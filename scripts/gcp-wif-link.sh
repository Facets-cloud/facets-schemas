#!/bin/bash
# Keyless GCP link via Workload Identity Federation. No service account keys.
# Usage: bash gcp-wif-link.sh <CP_HOST> '<ACCOUNT_NAME>' <WEBHOOK_ID> --grants grants.json [--project <id>] [--aws-account-id <12 digits>]
# Without --project the script asks which project to use, offering the shell's current one as the default.
set -euo pipefail

usage() { echo "Usage: $0 <CP_HOST> <ACCOUNT_NAME> <WEBHOOK_ID> --grants grants.json [--project <id>] [--aws-account-id <12 digits>]" >&2; exit 1; }
die() { echo "ERROR: $*" >&2; exit 1; }

[ "$#" -ge 5 ] || usage
CP_HOST=$1; ACCOUNT_NAME=$2; WEBHOOK_ID=$3; shift 3
GRANTS=""; AWS_ID=""; PROJECT_ID=""
while [ "$#" -gt 0 ]; do
  case "$1" in
    --grants) [ "$#" -ge 2 ] || usage; GRANTS=$2; shift 2 ;;
    --project) [ "$#" -ge 2 ] || usage; PROJECT_ID=$2; shift 2 ;;
    --aws-account-id) [ "$#" -ge 2 ] || usage; AWS_ID=$2; shift 2 ;;
    *) usage ;;
  esac
done

[[ "$CP_HOST" =~ ^[A-Za-z0-9]([A-Za-z0-9.-]*[A-Za-z0-9])?(:[0-9]{1,5})?$ ]] || die "invalid CP_HOST: $CP_HOST"
[[ "$ACCOUNT_NAME" =~ ^[a-z0-9]([a-z0-9-]*[a-z0-9])?$ ]] || die "ACCOUNT_NAME must be lowercase letters, digits and hyphens"
[ -n "$WEBHOOK_ID" ] || die "WEBHOOK_ID is empty"
[ -r "$GRANTS" ] || die "grants file not readable: ${GRANTS:-<missing --grants>}"
[ -z "$PROJECT_ID" ] || [[ "$PROJECT_ID" =~ ^[a-z][a-z0-9-]{4,28}[a-z0-9]$ ]] || die "invalid --project: $PROJECT_ID"

# JSON helpers: python3, else jq.
if command -v python3 >/dev/null 2>&1; then
  # jlist <key>: one array element per line; jstr <key>: string or empty
  jlist() { python3 -c 'import json,sys;[print(x) for x in json.load(open(sys.argv[1])).get(sys.argv[2]) or []]' "$GRANTS" "$1"; }
  jstr() { python3 -c 'import json,sys;print(json.load(open(sys.argv[1])).get(sys.argv[2]) or "")' "$GRANTS" "$1"; }
  jbody() { python3 -c 'import json,sys;k=["webhookId","name","project","projectNumber","serviceAccountEmail","workloadIdentityPool","workloadIdentityProvider","awsAccountId"];v=sys.argv[1:];print(json.dumps({"webhookId":v[0],"payload":dict([("authType","WORKLOAD_IDENTITY")]+list(zip(k[1:],v[1:])))},separators=(",",":")))' "$@"; }
elif command -v jq >/dev/null 2>&1; then
  jlist() { jq -r --arg k "$1" '.[$k] // [] | .[]' "$GRANTS"; }
  jstr() { jq -r --arg k "$1" '.[$k] // ""' "$GRANTS"; }
  jbody() { jq -nc --args '{webhookId:$ARGS.positional[0],payload:{authType:"WORKLOAD_IDENTITY",name:$ARGS.positional[1],project:$ARGS.positional[2],projectNumber:$ARGS.positional[3],serviceAccountEmail:$ARGS.positional[4],workloadIdentityPool:$ARGS.positional[5],workloadIdentityProvider:$ARGS.positional[6],awsAccountId:$ARGS.positional[7]}}' "$@"; }
else
  die "python3 or jq is required"
fi

MAPPING=$(jstr wifSubjectMapping)
[ -n "$MAPPING" ] || die "grants file has no wifSubjectMapping"
[ -n "$AWS_ID" ] || AWS_ID=$(jstr awsAccountId)
[[ "$AWS_ID" =~ ^[0-9]{12}$ ]] || die "AWS account id must be 12 digits (pass --aws-account-id)"
ROLES=(); while IFS= read -r l; do ROLES+=("$l"); done < <(jlist projectRoles)
APIS=();  while IFS= read -r l; do APIS+=("$l");  done < <(jlist apis)

# Project: --project wins; otherwise ask, defaulting to the shell's current project.
# Answers come from the terminal (FACETS_PROMPT_IN overrides it for tests).
if [ -z "$PROJECT_ID" ]; then
  CURRENT=$(gcloud config get-value project 2>/dev/null || true)
  [ "$CURRENT" = "(unset)" ] && CURRENT=""
  PROMPT_IN=${FACETS_PROMPT_IN:-/dev/tty}
  if [ -n "${FACETS_PROMPT_IN:-}" ] || { [ -t 1 ] && { : < /dev/tty; } 2>/dev/null; }; then
    echo "Fetching available projects..."
    PLIST=(); while IFS= read -r l; do PLIST+=("$l"); done < <(gcloud projects list --filter="NOT projectId:sys-*" --format='value(projectId)')
    [ "${#PLIST[@]}" -gt 0 ] || die "No projects are available. Ensure you're logged in."
    exec 3< "$PROMPT_IN"
    echo "Which project should Facets use?"
    for i in "${!PLIST[@]}"; do
      mark=""; [ "${PLIST[$i]}" = "$CURRENT" ] && mark="  (current)"
      printf '  %2d) %s%s\n' "$((i + 1))" "${PLIST[$i]}" "$mark"
    done
    while :; do
      if [ -n "$CURRENT" ]; then printf 'Number, or Enter for %s: ' "$CURRENT"; else printf 'Number: '; fi
      IFS= read -r -u 3 ans || die "no project chosen"
      if [ -z "$ans" ] && [ -n "$CURRENT" ]; then PROJECT_ID=$CURRENT; break; fi
      if [[ "$ans" =~ ^[0-9]+$ ]] && [ "$ans" -ge 1 ] && [ "$ans" -le "${#PLIST[@]}" ]; then PROJECT_ID=${PLIST[$((ans - 1))]}; break; fi
      echo "Enter a number from 1 to ${#PLIST[@]}."
    done
    exec 3<&-
  else
    [ -n "$CURRENT" ] || die "no project set; pass --project <id> or run: gcloud config set project <id>"
    echo "Using the current gcloud project $CURRENT (pass --project to choose another)."
    PROJECT_ID=$CURRENT
  fi
fi
[ -n "$PROJECT_ID" ] || die "no project selected"
PROJECT_NUMBER=$(gcloud projects describe "$PROJECT_ID" --format='value(projectNumber)')
[[ "$PROJECT_NUMBER" =~ ^[0-9]+$ ]] || die "could not get project number for $PROJECT_ID"
echo "Project: $PROJECT_ID ($PROJECT_NUMBER)"

# APIs
if [ "${#APIS[@]}" -gt 0 ]; then
  echo "Enabling ${#APIS[@]} APIs..."
  # Service Usage accepts at most 20 services per enable call.
  for ((i = 0; i < ${#APIS[@]}; i += 20)); do
    gcloud services enable "${APIS[@]:i:20}" --project "$PROJECT_ID"
  done
fi
gcloud services enable iam.googleapis.com iamcredentials.googleapis.com sts.googleapis.com --project "$PROJECT_ID"

# Service account (id: 6-30 chars, lowercase, no trailing hyphen)
SA_ID=$(printf 'facets-%s' "$ACCOUNT_NAME" | cut -c1-30)
SA_ID=${SA_ID%-}
SA_EMAIL="$SA_ID@$PROJECT_ID.iam.gserviceaccount.com"
if gcloud iam service-accounts describe "$SA_EMAIL" --project "$PROJECT_ID" >/dev/null 2>&1; then
  echo "Service account exists: $SA_EMAIL"
else
  gcloud iam service-accounts create "$SA_ID" --display-name "Facets $ACCOUNT_NAME" --project "$PROJECT_ID"
fi
for r in "${ROLES[@]}"; do
  gcloud projects add-iam-policy-binding "$PROJECT_ID" --member "serviceAccount:$SA_EMAIL" --role "$r" --condition=None >/dev/null
done

# Pool + provider
POOL=facets-cp-pool; PROVIDER=facets-aws-provider
COND="attribute.aws_account=='$AWS_ID'"
LOC=(--project "$PROJECT_ID" --location global)
STATE=$(gcloud iam workload-identity-pools describe "$POOL" "${LOC[@]}" --format='value(state)' 2>/dev/null || true)
case "$STATE" in
  "") gcloud iam workload-identity-pools create "$POOL" "${LOC[@]}" --display-name "Facets control plane pool" ;;
  DELETED) gcloud iam workload-identity-pools undelete "$POOL" "${LOC[@]}" ;;
  *) echo "Pool exists: $POOL" ;;
esac
if gcloud iam workload-identity-pools providers describe "$PROVIDER" --workload-identity-pool "$POOL" "${LOC[@]}" >/dev/null 2>&1; then
  verb=update-aws
else
  verb=create-aws
fi
gcloud iam workload-identity-pools providers "$verb" "$PROVIDER" --workload-identity-pool "$POOL" "${LOC[@]}" \
  --account-id "$AWS_ID" --attribute-mapping "$MAPPING" --attribute-condition "$COND"

# Let the AWS account impersonate the SA
MEMBER="principalSet://iam.googleapis.com/projects/$PROJECT_NUMBER/locations/global/workloadIdentityPools/$POOL/attribute.aws_account/$AWS_ID"
gcloud iam service-accounts add-iam-policy-binding "$SA_EMAIL" --project "$PROJECT_ID" \
  --role roles/iam.workloadIdentityUser --member "$MEMBER" >/dev/null

# Callback
BODY=$(jbody "$WEBHOOK_ID" "$ACCOUNT_NAME" "$PROJECT_ID" "$PROJECT_NUMBER" "$SA_EMAIL" "$POOL" "$PROVIDER" "$AWS_ID")
URL="${CP_SCHEME:-https}://$CP_HOST/public/v1/link-gcp"
RESP=$(mktemp); trap 'rm -f "$RESP"' EXIT
# The control plane verifies the account by impersonating the SA, and the
# workloadIdentityUser grant above takes up to ~2 minutes to propagate in IAM.
# A rejected callback leaves the link usable, so retry while verification fails.
for attempt in 1 2 3 4 5 6 7 8 9; do
  # No -f: curl drops the response body on an HTTP error with -f, and that body carries the reason.
  CODE=$(curl -sS --retry 3 -X POST -H 'Content-Type: application/json' -d "$BODY" -o "$RESP" -w '%{http_code}' "$URL") \
    || { echo "ERROR: could not reach the control plane at $CP_HOST" >&2; exit 1; }
  if [[ "$CODE" == 2* || $attempt -eq 9 ]] || ! grep -q "Invalid credentials" "$RESP"; then break; fi
  echo "Waiting for the IAM grant to propagate (attempt $attempt of 9)..."
  sleep 15
done
if [[ "$CODE" != 2* ]]; then
  echo "ERROR: control plane rejected the link (HTTP $CODE):" >&2; cat "$RESP" >&2; echo >&2
  echo "The project is set up; fix the issue above and run the same command again." >&2
  exit 1
fi
echo "Linked '$ACCOUNT_NAME': service account $SA_EMAIL, pool $POOL, provider $PROVIDER (AWS $AWS_ID), ${#ROLES[@]} roles, ${#APIS[@]} APIs."
