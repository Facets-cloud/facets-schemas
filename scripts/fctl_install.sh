#!/bin/bash
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

# Check if running on Alpine Linux
IS_ALPINE=false
if [ -f /etc/alpine-release ]; then
    IS_ALPINE=true
fi

# Normalize architecture name
if [ "$ARCH" = "aarch64" ]; then
    ARCH="arm64"
fi

# Select the correct download URL
case $OS in
    Linux)
        case $ARCH in
            arm64)
                URL=$URL_LINUX_ARM64
                ;;
            x86_64)
                URL=$URL_LINUX_X86_64
                ;;
            *)
                echo "Unsupported architecture: $ARCH"
                exit 1
                ;;
        esac
        ;;
    Darwin)
        case $ARCH in
            x86_64)
                URL=$URL_DARWIN_X64
                ;;
            arm64)
                URL=$URL_DARWIN_ARM64
                ;;
            *)
                echo "Unsupported architecture: $ARCH"
                exit 1
                ;;
        esac
        ;;
    *)
        echo "Unsupported OS: $OS"
        exit 1
        ;;
esac

# Function to install facetsctl
install_facetsctl() {
    echo "Downloading facetsctl..."
    mkdir -p "$INSTALL_DIR/facetsctl"
    
    # Install Node.js if running on Alpine Linux
    if [ "$IS_ALPINE" = true ]; then
        echo "Alpine Linux detected. Installing Node.js..."
        apk add nodejs
    fi
    
    curl -L "$URL" | tar -xz -C "$INSTALL_DIR/facetsctl" --strip-components=1
    chmod +x "$BIN_PATH"
    
    # Handle Node.js replacement if running on Alpine Linux
    if [ "$IS_ALPINE" = true ]; then
        echo "Replacing bundled Node.js..."
        rm -f "$HOME/facetsctl/bin/node"
        # Create empty dummy node file for backward compatibility
        touch "$HOME/facetsctl/bin/node"
    fi
    
    echo "facetsctl installed successfully in $INSTALL_DIR/facetsctl"
}

# Check if facetsctl is already installed and check version contains the correct version
if [ -x "$BIN_PATH" ]; then
    # Check if the installed version contains the desired version
    if $BIN_PATH --version 2>/dev/null | grep -q "$VERSION"; then
        echo "facetsctl $VERSION is already installed."
        exit 0
    else
        echo "Version mismatch or facetsctl not executable. Updating facetsctl..."
        install_facetsctl
    fi
else
    echo "facetsctl not installed. Installing..."
    install_facetsctl
fi

# Add facetsctl to the PATH in .bashrc if not already added
if ! grep -q "export PATH=\"$INSTALL_DIR/facetsctl/bin:\$PATH\"" "$HOME/.bashrc"; then
    echo "Adding facetsctl to PATH in .bashrc"
    echo "export PATH=\"$INSTALL_DIR/facetsctl/bin:\$PATH\"" >> "$HOME/.bashrc"
    export PATH="$INSTALL_DIR/facetsctl/bin":$PATH
    echo "Run 'source ~/.bashrc' to update the current session."
fi

echo "Installation complete."
