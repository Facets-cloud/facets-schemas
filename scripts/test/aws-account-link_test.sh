#!/usr/bin/env bash
# Stub test for the setup-link path of aws-account-link.sh (--policy). Stub aws, curl and
# sleep log their argv to $LOG; nothing reaches AWS or the network.
S="$(cd "$(dirname "$0")/.." && pwd)/aws-account-link.sh"
T=$(mktemp -d); trap 'rm -rf "$T"' EXIT
mkdir -p "$T/bin"
cat > "$T/bin/aws" <<'STUB'
#!/usr/bin/env bash
echo "aws $*" >> "$LOG"
case "$*" in
  "iam get-role"*) [ -n "${EXISTS:-}" ] && echo '{"Role":{"Arn":"arn:aws:iam::210847396512:role/acme"}}' && exit 0; exit 254 ;;
  "iam list-role-tags"*) echo "{\"Tags\":[{\"Key\":\"facets-setup-link\",\"Value\":\"${OWNER:-}\"}]}" ;;
  "iam create-role"*) echo '{"Role":{"Arn":"arn:aws:iam::210847396512:role/acme"}}' ;;
esac
STUB
cat > "$T/bin/curl" <<'STUB'
#!/usr/bin/env bash
echo "curl $*" >> "$LOG"
out=""; prev=""; failflag=""; for a in "$@"; do [ "$prev" = "-o" ] && out=$a; case "$a" in -f|-fsS|-fsSL|-fs) failflag=1;; esac; prev=$a; done
# Like real curl: with -f an HTTP error drops the body and exits 22; without -f the
# body is written and -w '%{http_code}' prints the status.
if [ -n "${CURL_FAIL:-}" ]; then
  [ -n "$failflag" ] && exit 22
  [ -n "$out" ] && echo '{"message":"bad link"}' > "$out"; printf 400; exit 0
fi
[ -n "$out" ] && echo '{}' > "$out"; printf 200; exit 0
STUB
printf '#!/usr/bin/env bash\necho "sleep $*" >> "$LOG"\n' > "$T/bin/sleep"
chmod +x "$T/bin/"*
echo '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Action":["s3:*"],"Resource":"*"}]}' > "$T/policy.json"
echo '{"Version":"2012-10-17"}' > "$T/bad-policy.json"

fail=0
ok() { grep -qF -- "$1" "$LOG" && echo "ok   $2" || { echo "FAIL $2"; fail=1; }; }
no() { grep -qF -- "$1" "$LOG" && { echo "FAIL $2"; fail=1; } || echo "ok   $2"; }
run() { LOG="$T/log"; : > "$LOG"; export LOG PATH="$T/bin:$PATH" CP_SCHEME=http; bash "$S" "$@" >"$T/out" 2>&1; }

run cp.example:8080 acme hook1 313496281593 --policy "$T/policy.json"; rc=$?
[ $rc = 0 ] && echo "ok   new role exit 0" || { echo "FAIL new role exit $rc"; cat "$T/out"; fail=1; }
ok "iam create-role --role-name acme" "role created"
ok "Key=facets-setup-link,Value=hook1" "role tagged with the link"
ok 'arn:aws:iam::313496281593:root' "trust names the Facets account"
ok '"sts:ExternalId":' "trust requires an external id"
ok "iam put-role-policy --role-name acme --policy-name acme-Policy --policy-document file://$T/policy.json" "policy from --policy attached"
no "facets-schemas/scripts/aws-policy.json" "hosted policy not downloaded"
ok "http://cp.example:8080/public/v1/link-aws" "callback URL"
no " -k " "no curl -k"
grep '^curl' "$LOG" | grep -qE '"webhookId":"hook1","payload":\{"externalId":"[0-9a-f-]{36}","iamRole":"arn:aws:iam::210847396512:role/acme","name":"acme"\}' \
  && echo "ok   exact callback body" || { echo "FAIL exact callback body"; grep '^curl' "$LOG"; fail=1; }
ext_trust=$(grep -o '"sts:ExternalId":"[^"]*"' "$LOG" | head -1 | cut -d'"' -f4)
grep '^curl' "$LOG" | grep -qF "\"externalId\":\"$ext_trust\"" && echo "ok   same external id in trust and callback" || { echo "FAIL external id mismatch"; fail=1; }

EXISTS=1 OWNER=hook1 run cp.example acme hook1 313496281593 --policy "$T/policy.json"; rc=$?
[ $rc = 0 ] && echo "ok   rerun exit 0" || { echo "FAIL rerun exit $rc"; cat "$T/out"; fail=1; }
ok "iam update-assume-role-policy --role-name acme" "rerun: trust updated"
no "iam create-role" "rerun: role not recreated"

EXISTS=1 OWNER=someone-else run cp.example acme hook1 313496281593 --policy "$T/policy.json"; rc=$?
[ $rc != 0 ] && grep -q "was not created by this setup link" "$T/out" && echo "ok   foreign role refused" || { echo "FAIL foreign role rc=$rc"; fail=1; }
no "update-assume-role-policy" "foreign role: trust untouched"
no "put-role-policy" "foreign role: policy untouched"
no "^curl" "foreign role: no callback"

CURL_FAIL=1 run cp.example acme hook1 313496281593 --policy "$T/policy.json"; rc=$?
[ $rc != 0 ] && grep -q "bad link" "$T/out" && echo "ok   rejected callback fails with the server message" || { echo "FAIL callback failure rc=$rc"; fail=1; }

for bad in "cp.example acme hook1 12 --policy $T/policy.json" "cp.example 'bad name' hook1 313496281593 --policy $T/policy.json" "cp.example acme '' 313496281593 --policy $T/policy.json" "cp.example acme hook1 313496281593 --policy /nonexistent" "cp.example acme hook1 313496281593 --policy $T/bad-policy.json" "'cp.example/x' acme hook1 313496281593 --policy $T/policy.json"; do
  eval "run $bad"; rc=$?
  [ $rc != 0 ] && ! grep -q '^aws iam create-role' "$LOG" && ! grep -q '^curl' "$LOG" && echo "ok   bad args rejected: $bad" || { echo "FAIL bad args: $bad"; fail=1; }
done

# The original 4-argument flow keeps its usage line and still rejects a wrong argument count.
run a b c; rc=$?
[ $rc = 1 ] && grep -q "Usage: .*<ACCOUNT_ID> \[--policy" "$T/out" && echo "ok   legacy arg count unchanged" || { echo "FAIL legacy usage rc=$rc"; fail=1; }

exit $fail
