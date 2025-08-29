# Terraform Provider Aliasing

A Python script for installing Terraform providers with custom aliasing support. This tool allows you to use multiple versions of the same provider under different names (e.g., `aws4`, `aws5`) in your Terraform configurations.

## Features

- **Provider Aliasing**: Install standard providers under custom names
- **Concurrent Downloads**: Fast, parallel provider downloads
- **Multi-Platform Support**: Automatic platform detection (Linux, macOS, Windows)
- **Local Filesystem Mirror**: Generates terraform.rc configuration for local provider usage
- **Retry Logic**: Automatic retry on download failures
- **Batch Installation**: Install multiple providers from a single configuration file

## Requirements

- Python 3.6+
- Terraform
- Internet connection for downloading providers

## Installation

No installation required. Simply download the script and run it with Python.

## Usage

### Basic Usage

```bash
python provider_aliasing.py --providers-json providers.json --terraform-dir ./my-project
```

### Command Line Arguments

- `--providers-json` (required): Path to JSON file containing provider configurations
- `--terraform-dir`: Target directory for provider installation (default: current directory)
- `--max-workers`: Maximum concurrent downloads (default: 5)

### Provider Configuration Format

Create a JSON file with an array of provider configurations:

```json
[
  {
    "source": "registry.terraform.io/hashicorp/aws5",
    "version": "5.0.0",
    "download_url": {
      "darwin_arm64": "https://releases.hashicorp.com/terraform-provider-aws/5.0.0/terraform-provider-aws_5.0.0_darwin_arm64.zip",
      "linux_amd64": "https://releases.hashicorp.com/terraform-provider-aws/5.0.0/terraform-provider-aws_5.0.0_linux_amd64.zip",
      "darwin_amd64": "https://releases.hashicorp.com/terraform-provider-aws/5.0.0/terraform-provider-aws_5.0.0_darwin_amd64.zip",
      "linux_arm64": "https://releases.hashicorp.com/terraform-provider-aws/5.0.0/terraform-provider-aws_5.0.0_linux_arm64.zip"
    }
  },
  {
    "source": "registry.terraform.io/hashicorp/aws4",
    "version": "4.53.0",
    "download_url": {
      "darwin_arm64": "https://releases.hashicorp.com/terraform-provider-aws/4.53.0/terraform-provider-aws_4.53.0_darwin_arm64.zip",
      "linux_amd64": "https://releases.hashicorp.com/terraform-provider-aws/4.53.0/terraform-provider-aws_4.53.0_linux_amd64.zip"
    }
  }
]
```

### Configuration Fields

- **source**: Provider source path (e.g., `registry.terraform.io/hashicorp/aws5`)
  - The last segment after `/` becomes the alias (e.g., `aws5`)
- **version**: Provider version
- **download_url**: Platform-specific download URLs
  - Supported platforms: `linux_amd64`, `linux_arm64`, `darwin_amd64`, `darwin_arm64`, `windows_amd64`

## How It Works

1. **Downloads Providers**: Fetches provider binaries from specified URLs
2. **Creates Directory Structure**: Organizes providers in `terraform.d/plugins/` following Terraform's expected layout
3. **Renames Binaries**: Renames provider binaries to match the alias (e.g., `terraform-provider-aws5`)
4. **Sets Permissions**: Makes provider binaries executable
5. **Generates terraform.rc**: Creates a CLI configuration file for local provider usage

## Directory Structure

After running the script, your directory will contain:

```
my-project/
├── terraform.rc                    # Generated CLI configuration
└── terraform.d/
    └── plugins/
        └── registry.terraform.io/
            └── hashicorp/
                ├── aws4/
                │   └── 4.53.0/
                │       └── darwin_arm64/
                │           └── terraform-provider-aws4
                └── aws5/
                    └── 5.0.0/
                        └── darwin_arm64/
                            └── terraform-provider-aws5
```

## Using with Terraform

1. **Set the CLI configuration**:
   ```bash
   export TF_CLI_CONFIG_FILE="/path/to/your/project/terraform.rc"
   ```

2. **Define providers in your Terraform configuration**:
   ```hcl
   terraform {
     required_providers {
       aws5 = {
         source  = "registry.terraform.io/hashicorp/aws5"
         version = "5.0.0"
       }
       aws4 = {
         source  = "registry.terraform.io/hashicorp/aws4"
         version = "4.53.0"
       }
     }
   }
   ```

3. **Run terraform init**:
   ```bash
   terraform init
   ```

## Example Workflow

```bash
# 1. Create provider configuration
cat > providers.json << EOF
[
  {
    "source": "registry.terraform.io/hashicorp/aws5",
    "version": "5.0.0",
    "download_url": {
      "darwin_arm64": "https://releases.hashicorp.com/terraform-provider-aws/5.0.0/terraform-provider-aws_5.0.0_darwin_arm64.zip",
      "linux_amd64": "https://releases.hashicorp.com/terraform-provider-aws/5.0.0/terraform-provider-aws_5.0.0_linux_amd64.zip"
    }
  }
]
EOF

# 2. Run the script
python provider_aliasing.py --providers-json providers.json --terraform-dir ./my-project

# 3. Set environment variable
export TF_CLI_CONFIG_FILE="$(pwd)/my-project/terraform.rc"

# 4. Initialize Terraform
cd my-project
terraform init
```

## Platform Detection

The script automatically detects your platform. Supported platforms:
- Linux (amd64, arm64, 386)
- macOS/Darwin (amd64, arm64)
- Windows (amd64)

Override platform detection with environment variables if needed.

## Use Cases

- **Multiple Provider Versions**: Use different versions of the same provider in one project
- **Testing**: Test infrastructure code against multiple provider versions
- **Migration**: Gradually migrate from one provider version to another
- **Air-gapped Environments**: Pre-install providers for environments without internet access
- **CI/CD**: Cache providers for faster CI/CD pipelines

## Troubleshooting

### Provider not found
Ensure the download URL is correct and accessible for your platform.

### Permission denied
The script automatically sets executable permissions. If issues persist, manually run:
```bash
chmod 755 terraform.d/plugins/*/*/*/*/terraform-provider-*
```

### Wrong platform detected
Check detected platform:
```python
python -c "import platform; print(platform.system(), platform.machine())"
```

## License

This script is provided as-is for use with Terraform provider management.