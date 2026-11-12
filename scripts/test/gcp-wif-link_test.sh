#!/bin/bash
# Stub-based test: no real gcloud/curl. EXISTS=1 makes describe calls succeed.
set -uo pipefail
S="$(cd "$(dirname "$0")/.." && pwd)/gcp-wif-link.sh"
T=$(mktemp -d); trap 'rm -rf "$T"' EXIT
mkdir "$T/bin"
cat > "$T/bin/gcloud" <<'S'
#!/bin/bash
printf '%s\n' "$*" >> "$LOG"
case "$*" in
  "config get-value project") echo my-proj ;;
  "projects describe"*) echo 123456 ;;
  *"pools describe"*"--format=value(state)"*) [ "${EXISTS:-}" = 1 ] && echo ACTIVE ;;
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
[ -n "$out" ] && echo '{}' > "$out"; printf 200; exit 0
S
chmod +x "$T/bin/"*
echo '{"projectRoles":["roles/owner"],"wifSubjectMapping":"google.subject=assertion.account,attribute.aws_account=assertion.account","apis":["compute.googleapis.com"],"awsAccountId":"313496281593"}' > "$T/full.json"
echo '{"projectRoles":["roles/viewer","roles/iam.securityReviewer"],"wifSubjectMapping":"google.subject=assertion.account,attribute.aws_account=assertion.account","apis":[]}' > "$T/ro.json"
fail=0
ok() { grep -qF -- "$1" "$LOG" && echo "ok   $2" || { echo "FAIL $2"; fail=1; }; }
no() { grep -qF -- "$1" "$LOG" && { echo "FAIL $2"; fail=1; } || echo "ok   $2"; }
run() { LOG="$T/log"; : > "$LOG"; export LOG PATH="$T/bin:$PATH" CP_SCHEME=http; "$S" "$@" >"$T/out" 2>&1; }

run cp.example:8080 acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && echo "ok   full exit 0" || { echo "FAIL full exit $rc"; cat "$T/out"; fail=1; }
ok "services enable compute.googleapis.com" "full: api enabled"
ok "--role roles/owner" "full: role bound"
ok "service-accounts create facets-acme" "full: SA created"
ok "pools create facets-aws-pool" "full: pool created"
ok "providers create-aws facets-aws-provider" "full: provider created"
ok "--account-id 313496281593" "full: account id"
ok "--attribute-condition attribute.aws_account=='313496281593'" "full: condition"
ok "--member principalSet://iam.googleapis.com/projects/123456/locations/global/workloadIdentityPools/facets-aws-pool/attribute.aws_account/313496281593" "full: principalSet"
ok '-d {"webhookId":"hook1","payload":{"authType":"WORKLOAD_IDENTITY","name":"acme","project":"my-proj","projectNumber":"123456","serviceAccountEmail":"facets-acme@my-proj.iam.gserviceaccount.com","workloadIdentityPool":"facets-aws-pool","workloadIdentityProvider":"facets-aws-provider","awsAccountId":"313496281593"}} -o' "full: exact POST body"
ok "http://cp.example:8080/public/v1/link-gcp" "full: URL"
no "keys create" "full: no keys create"
no " -k " "full: no curl -k"

run cp.example acme hook1 --grants "$T/ro.json" --aws-account-id 111122223333; rc=$?
[ $rc = 0 ] && echo "ok   ro exit 0" || { echo "FAIL ro exit $rc"; cat "$T/out"; fail=1; }
ok "--role roles/iam.securityReviewer" "ro: role bound"
no "services enable compute" "ro: no extra apis"
ok "--account-id 111122223333" "ro: aws id from flag"

EXISTS=1 run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc = 0 ] && echo "ok   rerun exit 0" || { echo "FAIL rerun exit $rc"; cat "$T/out"; fail=1; }
ok "providers update-aws facets-aws-provider" "rerun: provider updated"
no "service-accounts create" "rerun: SA not recreated"
no "pools create" "rerun: pool not recreated"

python3 -c "import json;print(json.dumps({'projectRoles':['roles/owner'],'wifSubjectMapping':'google.subject=assertion.account','apis':['api%d.googleapis.com'%i for i in range(41)],'awsAccountId':'313496281593'}))" > "$T/many.json"
run cp.example acme hook1 --grants "$T/many.json"; rc=$?
calls=$(grep -c 'services enable api' "$LOG"); widest=$(grep 'services enable api' "$LOG" | awk '{n=0; for(i=1;i<=NF;i++) if ($i ~ /^api[0-9]+\.googleapis\.com$/) n++; print n}' | sort -n | tail -1)
[ $rc = 0 ] && [ "$calls" = 3 ] && [ "$widest" -le 20 ] && grep -q 'api40.googleapis.com' "$LOG" && echo "ok   41 apis enabled in 3 calls of <=20" || { echo "FAIL batching rc=$rc calls=$calls widest=$widest"; fail=1; }

CURL_FAIL=1 run cp.example acme hook1 --grants "$T/full.json"; rc=$?
[ $rc != 0 ] && grep -q "bad link" "$T/out" && grep -q "HTTP 400" "$T/out" && echo "ok   rejected callback fails with the server message" || { echo "FAIL callback failure rc=$rc"; cat "$T/out"; fail=1; }

for bad in "cp.example Bad_Name hook1 --grants $T/full.json" "cp.example acme '' --grants $T/full.json" "cp.example acme hook1" "cp.example acme hook1 --grants /nonexistent" "cp.example acme hook1 --grants $T/ro.json --aws-account-id 12"; do
  eval "run $bad"; rc=$?
  [ $rc != 0 ] && ! grep -q '^curl' "$LOG" && echo "ok   bad args rejected: $bad" || { echo "FAIL bad args: $bad"; fail=1; }
done
exit $fail
