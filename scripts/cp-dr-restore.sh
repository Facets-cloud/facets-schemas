#!/bin/bash
# Facets Control Plane - disaster recovery restore.
#
# Restores the standby control plane in the DR region from the most recent
# backup package, then brings the control plane up.
#
# Usage:
#   curl -fsSL https://facets-cloud.github.io/facets-schemas/scripts/cp-dr-restore.sh | bash
#
# With options, pass them after -s --
#   curl -fsSL <url> | bash -s -- -n NAMESPACE -j CRONJOB
#
#   -n NAMESPACE   namespace holding the restore job   (default: default)
#   -j CRONJOB     name of the restore cronjob         (default: cp-dr-restore)
#   -y             skip the confirmation prompt
#
# Run this against the DR cluster, not the primary. The script checks which
# cluster your kubectl is pointed at and shows it before doing anything.

set -u

usage() {
	cat <<'USAGE'
Facets Control Plane - disaster recovery restore.

Restores the standby control plane in the DR region from the most recent
backup package, then brings the control plane up.

Usage:
  curl -fsSL https://facets-cloud.github.io/facets-schemas/scripts/cp-dr-restore.sh | bash

With options, pass them after -s --
  curl -fsSL <url> | bash -s -- -n NAMESPACE -j CRONJOB

  -n NAMESPACE   namespace holding the restore job   (default: default)
  -j CRONJOB     name of the restore cronjob         (default: cp-dr-restore)
  -y             skip the confirmation prompt

Run this against the DR cluster, not the primary. The script checks which
cluster your kubectl is pointed at and shows it before doing anything.
USAGE
}

NAMESPACE="default"
CRONJOB="cp-dr-restore"
ASSUME_YES="no"

while getopts ":n:j:yh" opt; do
	case "$opt" in
		n) NAMESPACE="$OPTARG" ;;
		j) CRONJOB="$OPTARG" ;;
		y) ASSUME_YES="yes" ;;
		h) usage; exit 0 ;;
		\?) echo "Unknown option: -$OPTARG" >&2; exit 1 ;;
		:) echo "Option -$OPTARG requires a value" >&2; exit 1 ;;
	esac
done

fail() {
	echo ""
	echo "FAILED: $1" >&2
	[ -n "${2:-}" ] && echo "        $2" >&2
	exit 1
}

echo "Facets Control Plane - disaster recovery restore"
echo "================================================"
echo ""
echo "Running preflight checks."
echo ""

# 1. kubectl present -------------------------------------------------------
printf "  %-38s" "kubectl available"
if ! command -v kubectl >/dev/null 2>&1; then
	echo "NO"
	fail "kubectl is not installed or not on PATH." \
	     "Install it from https://kubernetes.io/docs/tasks/tools/"
fi
echo "yes"

# 2. connected to a cluster ------------------------------------------------
printf "  %-38s" "cluster reachable"
if ! kubectl cluster-info >/dev/null 2>&1; then
	echo "NO"
	fail "kubectl cannot reach a cluster." \
	     "Check your kubeconfig and that you are on the right VPN or network."
fi
echo "yes"

CONTEXT=$(kubectl config current-context 2>/dev/null)
SERVER=$(kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}' 2>/dev/null)

# 3. namespace exists ------------------------------------------------------
printf "  %-38s" "namespace $NAMESPACE"
if ! kubectl get namespace "$NAMESPACE" >/dev/null 2>&1; then
	echo "NO"
	fail "Namespace $NAMESPACE does not exist in this cluster." \
	     "Use -n to specify the right namespace."
fi
echo "found"

# 4. this is a cluster with a restore job ----------------------------------
printf "  %-38s" "restore job present"
if ! kubectl -n "$NAMESPACE" get cronjob "$CRONJOB" >/dev/null 2>&1; then
	echo "NO"
	fail "No cronjob named $CRONJOB in namespace $NAMESPACE." \
	     "This cluster does not look like a Facets DR standby control plane. Check you are pointed at the DR cluster, not the primary."
fi
echo "found"

# 5. permission to run it --------------------------------------------------
printf "  %-38s" "permission to create jobs"
if [ "$(kubectl auth can-i create jobs -n "$NAMESPACE" 2>/dev/null)" != "yes" ]; then
	echo "NO"
	fail "You do not have permission to create jobs in namespace $NAMESPACE."
fi
echo "yes"

# 6. nothing already running ----------------------------------------------
printf "  %-38s" "no restore already running"
ACTIVE=$(kubectl -n "$NAMESPACE" get jobs \
	-o jsonpath="{range .items[?(@.status.active)]}{.metadata.name}{'\n'}{end}" 2>/dev/null \
	| grep "^${CRONJOB}" | head -1)
if [ -n "$ACTIVE" ]; then
	echo "NO"
	fail "A restore is already running: $ACTIVE" \
	     "Wait for it to finish, or follow it with: kubectl -n $NAMESPACE logs -f job/$ACTIVE"
fi
echo "confirmed"

# Details of what will be restored, read off the cronjob itself.
IMAGE=$(kubectl -n "$NAMESPACE" get cronjob "$CRONJOB" \
	-o jsonpath='{.spec.jobTemplate.spec.template.spec.containers[0].image}' 2>/dev/null)

echo ""
echo "Target"
echo "------"
echo "  Cluster    : $CONTEXT"
echo "  API server : $SERVER"
echo "  Namespace  : $NAMESPACE"
echo "  Restore job: $CRONJOB"
echo "  Image      : ${IMAGE##*/}"
echo ""
echo "This restores THIS control plane from the most recent backup package"
echo "and replaces its databases, secrets, Terraform state and modules."
echo "Existing data in this control plane will be overwritten."
echo ""

if [ "$ASSUME_YES" != "yes" ]; then
	# Read from the terminal, not stdin, so this still prompts when the script
	# is piped in from curl. /dev/tty can exist but be unusable (cron, CI), so
	# the read itself is the test, not [ -r /dev/tty ].
	printf "Type 'restore' to proceed: "
	REPLY=""
	if ! read -r REPLY < /dev/tty 2>/dev/null; then
		echo ""
		fail "No terminal available to confirm on." \
		     "Re-run with -y if you intend to skip the confirmation prompt."
	fi
	if [ "$REPLY" != "restore" ]; then
		echo "Aborted. Nothing has been changed."
		exit 1
	fi
	echo ""
fi

JOB="${CRONJOB}-$(date -u +%Y%m%d-%H%M%S)"
echo "Starting restore job: $JOB"
if ! kubectl -n "$NAMESPACE" create job "$JOB" --from="cronjob/$CRONJOB" >/dev/null 2>&1; then
	fail "Could not create the restore job."
fi

# Wait for the pod, then stream its output. The job prints its own progress,
# timings and post-restore checklist.
printf "Waiting for it to start"
POD=""
for _ in $(seq 1 60); do
	POD=$(kubectl -n "$NAMESPACE" get pods -l "job-name=$JOB" \
		-o jsonpath='{.items[0].metadata.name}' 2>/dev/null)
	if [ -n "$POD" ]; then
		PHASE=$(kubectl -n "$NAMESPACE" get pod "$POD" \
			-o jsonpath='{.status.phase}' 2>/dev/null)
		case "$PHASE" in Running|Succeeded|Failed) break ;; esac
	fi
	printf "."
	sleep 5
done
echo ""
echo ""

if [ -z "$POD" ]; then
	fail "The restore job did not start a pod." \
	     "Inspect it with: kubectl -n $NAMESPACE describe job/$JOB"
fi

kubectl -n "$NAMESPACE" logs -f "$POD" 2>/dev/null

# The pod's exit code is the real result; logs -f can return before it is set.
for _ in $(seq 1 12); do
	PHASE=$(kubectl -n "$NAMESPACE" get pod "$POD" -o jsonpath='{.status.phase}' 2>/dev/null)
	case "$PHASE" in Succeeded|Failed) break ;; esac
	sleep 5
done

if [ "$PHASE" = "Succeeded" ]; then
	exit 0
fi

echo ""
echo "The restore job did not complete successfully."
echo "Full output: kubectl -n $NAMESPACE logs $POD"
exit 1
