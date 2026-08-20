#!/usr/bin/env bash
#
# Install the raptor CLI (https://github.com/Facets-cloud/raptor-releases).
#
#   curl -fsSL https://facets-cloud.github.io/facets-schemas/scripts/raptor_install.sh | bash
#
# Environment:
#   RAPTOR_VERSION   release tag to install, or "latest" (default). Pin it in CI:
#                    a build that installs "latest" changes when raptor does.
#   INSTALL_DIR      where to put the binary. Defaults to /usr/local/bin when that is
#                    writable (directly or via sudo), otherwise $HOME/.local/bin.
#
# The successor to fctl_install.sh. raptor is a single static binary, so there is no
# tarball to unpack, no bundled Node to work around, and nothing to add to .bashrc
# unless the fallback directory is used.

set -euo pipefail

VERSION="${RAPTOR_VERSION:-latest}"
REPO_URL="https://github.com/Facets-cloud/raptor-releases/releases"

OS="$(uname -s)"
ARCH="$(uname -m)"

case "$OS" in
  Linux)  GOOS="linux" ;;
  Darwin) GOOS="darwin" ;;
  *)      echo "Unsupported OS: $OS (raptor ships linux, darwin and windows builds)" >&2; exit 1 ;;
esac

case "$ARCH" in
  x86_64|amd64)  GOARCH="amd64" ;;
  arm64|aarch64) GOARCH="arm64" ;;
  *)             echo "Unsupported architecture: $ARCH" >&2; exit 1 ;;
esac

ASSET="raptor-${GOOS}-${GOARCH}"
if [ "$VERSION" = "latest" ]; then
  URL="${REPO_URL}/latest/download/${ASSET}"
else
  URL="${REPO_URL}/download/${VERSION}/${ASSET}"
fi

# Pick a destination. A writable /usr/local/bin (with or without sudo) means raptor is
# on PATH for every shell; the fallback needs the caller to add it, which we say.
SUDO=""
if [ -n "${INSTALL_DIR:-}" ]; then
  :
elif [ -w /usr/local/bin ]; then
  INSTALL_DIR="/usr/local/bin"
elif command -v sudo >/dev/null 2>&1 && sudo -n true 2>/dev/null; then
  INSTALL_DIR="/usr/local/bin"
  SUDO="sudo"
else
  INSTALL_DIR="$HOME/.local/bin"
fi

TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

echo "Downloading ${ASSET} (${VERSION}) from ${URL}"
if ! curl -fsSL -o "$TMP/raptor" "$URL"; then
  echo "Download failed. Check that ${VERSION} is a released tag: ${REPO_URL}" >&2
  exit 1
fi
chmod +x "$TMP/raptor"

$SUDO mkdir -p "$INSTALL_DIR"
$SUDO mv "$TMP/raptor" "$INSTALL_DIR/raptor"

echo "raptor installed to ${INSTALL_DIR}/raptor"

case ":${PATH}:" in
  *":${INSTALL_DIR}:"*) ;;
  *) echo "NOTE: ${INSTALL_DIR} is not on your PATH. Add it:"
     echo "      export PATH=\"${INSTALL_DIR}:\$PATH\"" ;;
esac

# Authenticate with environment variables rather than a login: they take priority over
# any stored profile, so nothing is written to disk. In CI that matters.
cat <<'EOF'

Next: authenticate. In CI, set these three and skip `raptor login` entirely —
they win over any stored profile, so no credentials file is written:

  export CONTROL_PLANE_URL="https://<your-org>.console.facets.cloud"
  export FACETS_USERNAME="<your username>"
  export FACETS_TOKEN="<your access token>"

Then: raptor whoami
EOF
