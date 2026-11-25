#!/bin/bash
# Stub-based test: no real gcloud/curl. EXISTS=1 makes describe calls succeed.
set -uo pipefail
S="$(cd "$(dirname "$0")/.." && pwd)/gcp-wif-link.sh"
T=$(mktemp -d); trap 'rm -rf "$T"' EXIT
mkdir "$T/bin"
cat > "$T/bin/gcloud" <<'S'
#!/bin/bash
printf '%s\n' "$*" >> "$LOG"
# SA_LAG=n: the first n add-iam-policy-binding calls fail, as right after the SA is created.
if [ -n "${SA_LAG:-}" ] && [[ "$*" == *add-iam-policy-binding* ]]; then
  [ "$(grep -c 'add-iam-policy-binding' "$LOG")" -le "$SA_LAG" ] && { echo "INVALID_ARGUMENT: Service account does not exist." >&2; exit 1; }
fi
case "$*" in
  "config get-value project") [ -n "${NOPROJ:-}" ] && echo "(unset)" || echo my-proj ;;
  "projects list"*) printf '%s\n' other-proj my-proj ;;
  "projects describe"*) echo 123456 ;;
  *"pools describe"*"--format=value(state)"*) [ "${EXISTS:-}" = 1 ] && echo ACTIVE ;;
  *get-iam-policy*) [ -n "${OLDBIND:-}" ] && echo "principalSet://iam.googleapis.com/projects/123456/locations/global/workloadIdentityPools/facets-cp-pool/attribute.aws_account/313496281593" ;;
  *describe*) [ "${EXISTS:-}" = 1 ] || exit 1 ;;
esac
exit 0
S
cat > "$T/bin/curl" <<'S'
#!/bin/bash
printf 'curl %s\n' "$*" >> "$LOG"
# Like real curl: with -f an HTTP error drops the body and exits 22; without -f the
# body is written and -w '%{http_code}' prints the status.
out=""; prev=""; failflag=""; for a in "$@"; do [ "$prev" = "-o" ] && out=$a; case "$a" in -f|-fsS|-fsSL|-fs) failflag=1;; esac; prev=$a; done
if [ -n "${CURL_FAIL:-}" ]; then
  [ -n "$failflag" ] && exit 22
  [ -n "$out" ] && echo '{"message":"bad link"}' > "$out"; printf 400; exit 0
fi
# IAM_LAG=n: the first n callbacks fail verification, as while a grant propagates.
if [ -n "${IAM_LAG:-}" ]; then
  n=$(grep -c '^curl .*link-gcp' "$LOG")
  if [ "$n" -le "$IAM_LAG" ]; then
    echo '{"message":"Unable to add account: Account Verification failed: Invalid credentials"}' > "$out"; printf 400; exit 0
  fi
fi
[ -n "$out" ] && echo '{}' > "$out"; printf 200; exit 0
S
printf '#!/bin/bash\nprintf "sleep %%s\\n" "$*" >> "$LOG"\n' > "$T/bin/sleep"
chmod +x "$T/bin/"*
WIF='"oidcIssuer":"https://oidc.eks.us-east-2.amazonaws.com/id/8FB31AB8","oidcAudience":"facets-gcp-wif","workloadIdentityProvider":"facets-k8s-0123456789ab","wifSubjectMapping":"google.subject=assertion.sub","k8sSubjects":["system:serviceaccount:default:release-pod-sa-1234","system:serviceaccount:facets:release-pod-sa-1234","system:serviceaccount:default:release-pod-sa-1234"]'
echo '{"projectRoles":["roles/owner"],"apis":["compute.googleapis.com"],'"$WIF"'}' > "$T/full.json"
echo '{"projectRoles":["roles/viewer","roles/iam.securityReviewer"],"apis":[],'"$WIF"'}' > "$T/ro.json"
fail=0
ok() { grep -qF -- "$1" "$LOG" && echo "ok   $2" || { echo "FAIL $2"; fail=1; }; }
no() { grep -qF -- "$1" "$LOG" && { echo "FAIL $2"; fail=1; } || echo "ok   $2"; }
run() { LOG="$T/log"; : > "$LOG"; export LOG PATH="$T/bin:$PATH" CP_SCHEME=http; "$S" "$@" >"$T/out" 2>&1; }

run cp.example:8080 acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && echo "ok   full exit 0" || { echo "FAIL full exit $rc"; cat "$T/out"; fail=1; }
ok "services enable compute.googleapis.com" "full: api enabled"
ok "--role roles/owner" "full: role bound"
ok "service-accounts create facets-acme" "full: SA created"
ok "pools create facets-cp-pool" "full: pool created"
ok "providers create-oidc facets-k8s-0123456789ab --workload-identity-pool facets-cp-pool --project my-proj --location global --issuer-uri https://oidc.eks.us-east-2.amazonaws.com/id/8FB31AB8 --allowed-audiences facets-gcp-wif --attribute-mapping google.subject=assertion.sub" "full: OIDC provider on the cluster issuer, one audience"
ok "--member principal://iam.googleapis.com/projects/123456/locations/global/workloadIdentityPools/facets-cp-pool/subject/system:serviceaccount:default:release-pod-sa-1234" "full: release SA bound"
ok "--member principal://iam.googleapis.com/projects/123456/locations/global/workloadIdentityPools/facets-cp-pool/subject/system:serviceaccount:facets:release-pod-sa-1234" "full: CRD-namespace release SA bound"
[ "$(grep -c 'subject/system:serviceaccount:default:release-pod-sa-1234' "$LOG")" = 1 ] && echo "ok   full: duplicate subject bound once" || { echo "FAIL duplicate subject"; fail=1; }
no "principalSet://" "full: no set-wide binding"
no "create-aws" "full: no AWS provider"
no "attribute.aws_" "full: no AWS attributes"
no "remove-iam-policy-binding" "full: nothing to remove"
ok '-d {"webhookId":"hook1","payload":{"authType":"WORKLOAD_IDENTITY","name":"acme","project":"my-proj","projectNumber":"123456","serviceAccountEmail":"facets-acme@my-proj.iam.gserviceaccount.com","workloadIdentityPool":"facets-cp-pool","workloadIdentityProvider":"facets-k8s-0123456789ab"}} -o' "full: exact POST body"
ok "http://cp.example:8080/public/v1/link-gcp" "full: URL"
no "keys create" "full: no keys create"
grep -q 'trusts system:serviceaccount:default:release-pod-sa-1234 system:serviceaccount:facets:release-pod-sa-1234)' "$T/out" && echo "ok   full: summary names the trusted accounts" || { echo "FAIL summary"; cat "$T/out"; fail=1; }

# Grants are validated before anything changes in the project.
for bad in 'oidcIssuer=http://10.0.0.1/x' 'oidcIssuer=https://evil.example/x y' 'oidcAudience=' 'workloadIdentityProvider=Bad_Provider' 'k8sSubjects=["system:serviceaccounts:default"]' 'k8sSubjects=["system:serviceaccount:default:a b"]' 'k8sSubjects=[]'; do
  k=${bad%%=*}; v=${bad#*=}
  python3 -c "import json,sys;d=json.load(open('$T/full.json'));v=sys.argv[2];d[sys.argv[1]]=json.loads(v) if v.startswith('[') else v;print(json.dumps(d))" "$k" "$v" > "$T/bad.json"
  run cp.example acme hook1 --grants "$T/bad.json"; rc=$?
  [ $rc != 0 ] && ! grep -q 'service-accounts create' "$LOG" && ! grep -q '^curl' "$LOG" && echo "ok   bad grants refused before any change: $bad" || { echo "FAIL bad grants $bad rc=$rc"; cat "$T/out"; fail=1; }
done
no " -k " "full: no curl -k"

# Project choice: --project wins, otherwise the user is asked with the current project as default.
run cp.example acme hook1 --grants "$T/full.json" --project chosen-proj-1; rc=$?
[ $rc = 0 ] && grep -q '"project":"chosen-proj-1"' "$LOG" && ! grep -q '^projects list' "$LOG" && echo "ok   --project used without asking" || { echo "FAIL --project rc=$rc"; fail=1; }

printf '\n' > "$T/in"; FACETS_PROMPT_IN="$T/in" run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && grep -q 'my-proj  (current)' "$T/out" && grep -q 'Enter for my-proj' "$T/out" && grep -q '"project":"my-proj"' "$LOG" && echo "ok   asks even when a project is set; Enter keeps the current one" || { echo "FAIL prompt default rc=$rc"; cat "$T/out"; fail=1; }

printf '1\n' > "$T/in"; FACETS_PROMPT_IN="$T/in" run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && grep -q '"project":"other-proj"' "$LOG" && echo "ok   picking a number switches project" || { echo "FAIL prompt pick rc=$rc"; fail=1; }

printf '9\nx\n2\n' > "$T/in"; FACETS_PROMPT_IN="$T/in" run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && [ "$(grep -c 'Enter a number from 1 to 2' "$T/out")" = 2 ] && grep -q '"project":"my-proj"' "$LOG" && echo "ok   bad answers re-ask" || { echo "FAIL prompt retry rc=$rc"; cat "$T/out"; fail=1; }

printf '\n2\n' > "$T/in"; NOPROJ=1 FACETS_PROMPT_IN="$T/in" run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && ! grep -q '(current)' "$T/out" && grep -q '"project":"my-proj"' "$LOG" && echo "ok   no current project: Enter alone is not accepted" || { echo "FAIL no-current prompt rc=$rc"; cat "$T/out"; fail=1; }

run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && grep -q 'Using the current gcloud project my-proj' "$T/out" && echo "ok   no terminal: uses the current project and says so" || { echo "FAIL non-interactive rc=$rc"; fail=1; }

NOPROJ=1 run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc != 0 ] && grep -q 'pass --project' "$T/out" && echo "ok   no terminal and no project: clear error" || { echo "FAIL non-interactive no project rc=$rc"; fail=1; }

run cp.example acme hook1 --grants "$T/full.json" --project Bad_Proj; rc=$?
[ $rc != 0 ] && grep -q 'invalid --project' "$T/out" && echo "ok   invalid --project rejected" || { echo "FAIL invalid --project rc=$rc"; fail=1; }

run cp.example acme hook1 --grants "$T/ro.json"; rc=$?
[ $rc = 0 ] && echo "ok   ro exit 0" || { echo "FAIL ro exit $rc"; cat "$T/out"; fail=1; }
ok "--role roles/iam.securityReviewer" "ro: role bound"
no "services enable compute" "ro: no extra apis"

EXISTS=1 run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && echo "ok   rerun exit 0" || { echo "FAIL rerun exit $rc"; cat "$T/out"; fail=1; }
ok "providers update-oidc facets-k8s-0123456789ab" "rerun: provider updated"
no "service-accounts create" "rerun: SA not recreated"
no "pools create" "rerun: pool not recreated"

python3 -c "import json;d=json.load(open('$T/full.json'));d['apis']=['api%d.googleapis.com'%i for i in range(41)];print(json.dumps(d))" > "$T/many.json"
run cp.example acme hook1 --grants "$T/many.json"; rc=$?
calls=$(grep -c 'services enable api' "$LOG"); widest=$(grep 'services enable api' "$LOG" | awk '{n=0; for(i=1;i<=NF;i++) if ($i ~ /^api[0-9]+\.googleapis\.com$/) n++; print n}' | sort -n | tail -1)
[ $rc = 0 ] && [ "$calls" = 3 ] && [ "$widest" -le 20 ] && grep -q 'api40.googleapis.com' "$LOG" && echo "ok   41 apis enabled in 3 calls of <=20" || { echo "FAIL batching rc=$rc calls=$calls widest=$widest"; fail=1; }

IAM_LAG=2 run cp.example acme hook1 --grants "$T/full.json"; rc=$?
calls=$(grep -c '^curl .*link-gcp' "$LOG"); waits=$(grep -c '^sleep 15' "$LOG")
[ $rc = 0 ] && [ "$calls" = 3 ] && [ "$waits" = 2 ] && echo "ok   retries the callback while the IAM grant propagates" || { echo "FAIL IAM lag rc=$rc calls=$calls waits=$waits"; fail=1; }

IAM_LAG=99 run cp.example acme hook1 --grants "$T/full.json"; rc=$?
calls=$(grep -c '^curl .*link-gcp' "$LOG")
[ $rc != 0 ] && [ "$calls" = 9 ] && grep -q "Invalid credentials" "$T/out" && echo "ok   gives up after 9 callbacks with the server message" || { echo "FAIL IAM give-up rc=$rc calls=$calls"; fail=1; }

CURL_FAIL=1 run cp.example acme hook1 --grants "$T/full.json"; rc=$?
calls=$(grep -c '^curl .*link-gcp' "$LOG")
[ "$calls" = 1 ] && echo "ok   other rejections are not retried" || { echo "FAIL non-IAM rejection retried calls=$calls"; fail=1; }
[ $rc != 0 ] && grep -q "bad link" "$T/out" && grep -q "HTTP 400" "$T/out" && echo "ok   rejected callback fails with the server message" || { echo "FAIL callback failure rc=$rc"; cat "$T/out"; fail=1; }

OLDBIND=1 run cp.example acme hook1 --grants "$T/full.json"
ok "remove-iam-policy-binding facets-acme@my-proj.iam.gserviceaccount.com --project my-proj --role roles/iam.workloadIdentityUser --member principalSet://iam.googleapis.com/projects/123456/locations/global/workloadIdentityPools/facets-cp-pool/attribute.aws_account/313496281593" "rerun: old AWS-based binding removed"
grep -q 'Removing old AWS-based binding' "$T/out" && echo "ok   removal logged" || { echo "FAIL removal log"; fail=1; }

echo '{"projectRoles":["roles/owner"],"apis":[]}' > "$T/nomode.json"
for bad in "cp.example acme hook1 --grants $T/full.json --aws-role-arn arn:aws:iam::111122223333:role/x" "cp.example acme hook1 --grants $T/nomode.json" "cp.example Bad_Name hook1 --grants $T/full.json" "cp.example acme '' --grants $T/full.json" "cp.example acme hook1" "cp.example acme hook1 --grants /nonexistent"; do
  eval "run $bad"; rc=$?
  [ $rc != 0 ] && ! grep -q '^curl' "$LOG" && echo "ok   bad args rejected: $bad" || { echo "FAIL bad args: $bad"; fail=1; }
done

# Impersonation mode
echo '{"projectRoles":["roles/owner","roles/iam.serviceAccountTokenCreator"],"apis":["compute.googleapis.com"],"impersonatorServiceAccount":"facets-root-sa@cp-proj.iam.gserviceaccount.com"}' > "$T/imp.json"
run cp.example acme hook1 --grants "$T/imp.json"; rc=$?
[ $rc = 0 ] && echo "ok   imp exit 0" || { echo "FAIL imp exit $rc"; cat "$T/out"; fail=1; }
no "workload-identity-pools" "imp: no pool/provider calls"
no "workloadIdentityUser" "imp: no workloadIdentityUser"
no "sts.googleapis.com" "imp: sts not enabled"
ok "service-accounts create facets-acme" "imp: SA created"
ok "services enable iam.googleapis.com iamcredentials.googleapis.com" "imp: iamcredentials enabled"
ok "service-accounts add-iam-policy-binding facets-acme@my-proj.iam.gserviceaccount.com --project my-proj --role roles/iam.serviceAccountTokenCreator --member serviceAccount:facets-root-sa@cp-proj.iam.gserviceaccount.com" "imp: tokenCreator on the SA only"
ok '-d {"webhookId":"hook1","payload":{"authType":"IMPERSONATION","name":"acme","project":"my-proj","projectNumber":"123456","serviceAccountEmail":"facets-acme@my-proj.iam.gserviceaccount.com","impersonatorServiceAccount":"facets-root-sa@cp-proj.iam.gserviceaccount.com"}} -o' "imp: exact POST body"
grep -q 'Linked.*impersonation mode' "$T/out" && echo "ok   imp: summary names mode" || { echo "FAIL imp summary"; fail=1; }

IAM_LAG=2 run cp.example acme hook1 --grants "$T/imp.json"; rc=$?
[ $rc = 0 ] && [ "$(grep -c '^curl .*link-gcp' "$LOG")" = 3 ] && echo "ok   imp: callback retried on Invalid credentials" || { echo "FAIL imp retry rc=$rc"; fail=1; }

run cp.example acme hook1 --grants "$T/full.json"
grep -q 'Linked.*WIF mode' "$T/out" && echo "ok   wif: summary names mode" || { echo "FAIL wif summary"; cat "$T/out"; fail=1; }

echo '{"projectRoles":[],"impersonatorServiceAccount":"a@p.iam.gserviceaccount.com","oidcIssuer":"https://oidc.example/id/1"}' > "$T/both.json"
echo '{"projectRoles":[],"impersonatorServiceAccount":"evil@example.com"}' > "$T/badimp.json"
for c in "both.json::both oidcIssuer" "badimp.json::invalid impersonatorServiceAccount"; do
  f=${c%%:*}; rest=${c#*:}; flag=${rest%%:*}; msg=${rest#*:}
  # shellcheck disable=SC2086
  run cp.example acme hook1 --grants "$T/$f" $flag; rc=$?
  [ $rc != 0 ] && grep -q "$msg" "$T/out" && ! grep -q '^curl' "$LOG" && echo "ok   rejected: $msg" || { echo "FAIL $msg rc=$rc"; cat "$T/out"; fail=1; }
done
# Same project as the control plane: supported, but a name resolving to the impersonator is refused.
echo '{"projectRoles":["roles/owner"],"apis":[],"impersonatorServiceAccount":"facets-release-pod@my-proj.iam.gserviceaccount.com"}' > "$T/same.json"
run cp.example acme hook1 --grants "$T/same.json"; rc=$?
[ $rc = 0 ] && grep -qF -- '--member serviceAccount:facets-release-pod@my-proj.iam.gserviceaccount.com' "$LOG" && echo "ok   same-project link works" || { echo "FAIL same-project rc=$rc"; cat "$T/out"; fail=1; }
run cp.example release-pod hook1 --grants "$T/same.json"; rc=$?
[ $rc != 0 ] && grep -q "control plane's own service account" "$T/out" && ! grep -q "add-iam-policy-binding" "$LOG" && ! grep -q '^curl' "$LOG" && echo "ok   same-project: impersonator name refused before any grant" || { echo "FAIL same-project collision rc=$rc"; cat "$T/out"; fail=1; }
# A fresh SA is not visible to IAM at once: bindings retry, then the link still goes through.
SA_LAG=2 run cp.example acme hook1 --grants "$T/imp.json"; rc=$?
[ $rc = 0 ] && [ "$(grep -c '^sleep 10' "$LOG")" = 2 ] && grep -q 'link-gcp' "$LOG" && echo "ok   binding retried while the new SA propagates" || { echo "FAIL SA_LAG=2 rc=$rc"; cat "$T/out"; fail=1; }
SA_LAG=99 run cp.example acme hook1 --grants "$T/imp.json"; rc=$?
[ $rc != 0 ] && [ "$(grep -c '^sleep 10' "$LOG")" = 5 ] && ! grep -q 'link-gcp' "$LOG" && grep -q 'could not grant' "$T/out" && echo "ok   gives up after 6 tries, no callback" || { echo "FAIL SA_LAG=99 rc=$rc"; cat "$T/out"; fail=1; }
exit $fail
