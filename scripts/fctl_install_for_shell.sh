#!/bin/sh

# Define the version to install
VERSION="latest"
INSTALL_DIR="$HOME"
BIN_PATH="$INSTALL_DIR/facetsctl/bin/facetsctl"

# URLs for different architectures
URL_LINUX_ARM64="https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/v3/production/$VERSION/facetsctl-linux-arm64.tar.gz"
URL_LINUX_X86_64="https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/v3/production/$VERSION/facetsctl-linux-x64.tar.gz"
URL_DARWIN_X64="https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/v3/production/$VERSION/facetsctl-darwin-x64.tar.xz"
URL_DARWIN_ARM64="https://facets-cf-templates.s3.amazonaws.com/oclif-tarballs/v3/production/$VERSION/facetsctl-darwin-arm64.tar.gz"

# Detect OS and architecture
OS="$(uname -s)"
ARCH="$(uname -m)"

# Select the correct download URL
case "$OS" in
    Linux)
        case "$ARCH" in
            arm64)
                URL="$URL_LINUX_ARM64"
                ;;
            x86_64)
                URL="$URL_LINUX_X86_64"
                ;;
            *)
                printf "Unsupported architecture: %s\n" "$ARCH"
                exit 1
                ;;
        esac
        ;;
    Darwin)
        case "$ARCH" in
            x86_64)
                URL="$URL_DARWIN_X64"
                ;;
            arm64)
                URL="$URL_DARWIN_ARM64"
                ;;
            *)
                printf "Unsupported architecture: %s\n" "$ARCH"
                exit 1
                ;;
        esac
        ;;
    *)
        printf "Unsupported OS: %s\n" "$OS"
        exit 1
        ;;
esac

# Function to install facetsctl
install_facetsctl() {
    printf "Downloading facetsctl...\n"
    mkdir -p "$INSTALL_DIR/facetsctl"
    curl -L "$URL" | tar -xz -C "$INSTALL_DIR/facetsctl" --strip-components=1
    chmod +x "$BIN_PATH"
    printf "facetsctl installed successfully in %s/facetsctl\n" "$INSTALL_DIR"
}

# Check if facetsctl is already installed and check if the version matches
if [ -x "$BIN_PATH" ]; then
    # Check if the installed version contains the desired version
    if "$BIN_PATH" --version 2>/dev/null | grep -q "$VERSION"; then
        printf "facetsctl %s is already installed.\n" "$VERSION"
        exit 0
    else
        printf "Version mismatch or facetsctl not executable. Updating facetsctl...\n"
        install_facetsctl
    fi
else
    printf "facetsctl not installed. Installing...\n"
    install_facetsctl
fi

# Add facetsctl to the PATH in .profile or .bashrc if not already added
if ! grep -q "export PATH=\"$INSTALL_DIR/facetsctl/bin:\$PATH\"" "$HOME/.profile"; then
    printf "Adding facetsctl to PATH in .profile\n"
    printf "export PATH=\"$INSTALL_DIR/facetsctl/bin:\$PATH\"\n" >> "$HOME/.profile"
    export PATH="$INSTALL_DIR/facetsctl/bin:$PATH"
    printf "Run 'source ~/.profile' to update the current session.\n"
fi

printf "Installation complete.\n"
