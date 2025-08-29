#!/usr/bin/env python3

import argparse
import os
import sys
import urllib.request
import urllib.parse
from pathlib import Path
import zipfile
import tempfile
import platform
import json
import stat
import concurrent.futures


def get_terraform_platform():
    """
    Determine the Terraform platform string based on the current system
    
    Returns:
        str: Platform string in format 'os_arch' (e.g., 'linux_amd64', 'darwin_arm64')
    """
    os_map = {
        'Linux': 'linux',
        'Darwin': 'darwin',
        'Windows': 'windows'
    }
    
    arch_map = {
        'x86_64': 'amd64',
        'AMD64': 'amd64',
        'arm64': 'arm64',
        'aarch64': 'arm64',
        'i386': '386',
        'i686': '386'
    }
    
    system_os = platform.system()
    system_arch = platform.machine()
    
    terraform_os = os_map.get(system_os, system_os.lower())
    terraform_arch = arch_map.get(system_arch, system_arch.lower())
    
    return f"{terraform_os}_{terraform_arch}"


def create_provider_directory(terraform_dir, source, version, platform_string=None):
    """Create the provider directory structure with nested source paths"""
    if platform_string is None:
        platform_string = get_terraform_platform()
    
    provider_path = Path(terraform_dir) / "terraform.d" / "plugins" / source / version / platform_string
    provider_path.mkdir(parents=True, exist_ok=True)
    return provider_path


def download_file(url, destination, retry=True):
    """Download file from URL to destination with retry logic"""
    try:
        urllib.request.urlretrieve(url, destination)
        print(f"Downloaded to {destination}")
        return True
    except Exception as e:
        if retry:
            print(f"Initial download failed, retrying with verbose output...")
            try:
                req = urllib.request.Request(url)
                with urllib.request.urlopen(req) as response:
                    with open(destination, 'wb') as out_file:
                        out_file.write(response.read())
                print(f"Downloaded to {destination}")
                return True
            except Exception as e2:
                print(f"Download failed: {e2}")
                return False
        else:
            print(f"Download failed: {e}")
            return False


def extract_archive(archive_path, destination_dir):
    """Extract archive file (zip or tar.gz) to destination directory"""
    try:
        if archive_path.suffix.lower() == '.zip':
            with zipfile.ZipFile(archive_path, 'r') as zip_ref:
                zip_ref.extractall(destination_dir)
        else:
            print(f"Unsupported archive format: {archive_path.suffix}")
            return False
        
        # Set executable permissions on extracted files
        for file in destination_dir.iterdir():
            if file.is_file() and 'terraform-provider' in file.name:
                os.chmod(file, stat.S_IRWXU | stat.S_IRGRP | stat.S_IXGRP | stat.S_IROTH | stat.S_IXOTH)
                print(f"Set executable permissions on {file.name}")
        
        print(f"Extracted to {destination_dir}")
        return True
    except Exception as e:
        print(f"Error extracting archive: {e}")
        return False


def install_provider(download_url, source, version, terraform_dir=".", platform_override=None):
    """
    Install a Terraform provider with aliasing support
    
    Args:
        download_url (dict): Dictionary mapping platforms to URLs
        source (str): Provider source (e.g., 'registry.terraform.io/hashicorp/aws')
        version (str): Provider version (e.g., '5.0.0')
        terraform_dir (str): Terraform directory path (default: current directory)
        platform_override (str): Override platform detection (e.g., 'linux_amd64')
    
    Returns:
        bool: True if successful, False otherwise
    """
    terraform_dir = Path(terraform_dir)
    if not terraform_dir.exists():
        print(f"Error: Terraform directory '{terraform_dir}' does not exist")
        return False
    
    # Extract alias from source (last part after /)
    alias = source.split('/')[-1]
    
    # Determine platform
    platform_string = platform_override or get_terraform_platform()
    print(f"Detected platform: {platform_string}")
    
    # Select URL for current platform
    if platform_string not in download_url:
        available_platforms = list(download_url.keys())
        print(f"Error: No URL found for platform '{platform_string}'")
        print(f"Available platforms: {available_platforms}")
        return False
    
    selected_url = download_url[platform_string]
    
    # Create provider directory structure
    provider_dir = create_provider_directory(terraform_dir, source, version, platform_string)
    
    # Check if provider already exists with the expected alias name
    expected_binary = provider_dir / f"terraform-provider-{alias}"
    if expected_binary.exists():
        print(f"Provider already exists at {expected_binary}")
        return True
    
    print(f"Selected URL for {platform_string}: {selected_url}")
    print(f"Provider directory: {provider_dir}")
    
    # Parse URL to get filename
    parsed_url = urllib.parse.urlparse(selected_url)
    filename = Path(parsed_url.path).name
    
    if not filename:
        print("Error: Could not determine filename from URL")
        return False
    
    # Download to temporary file first
    with tempfile.TemporaryDirectory() as temp_dir:
        temp_file = Path(temp_dir) / filename
        
        if not download_file(selected_url, temp_file):
            return False
        
        # Extract or copy to provider directory
        if not extract_archive(temp_file, provider_dir):
            return False
        
        # Rename the extracted provider binary to match the alias
        for file in provider_dir.iterdir():
            if file.is_file() and file.name.startswith('terraform-provider-') and file.name != f"terraform-provider-{alias}":
                new_name = provider_dir / f"terraform-provider-{alias}"
                file.rename(new_name)
                print(f"Renamed provider binary to {new_name.name}")
                break
    
    print(f"Successfully installed provider {source} version {version}")
    print(f"Provider location: {provider_dir}")
    return True


def install_multiple_providers(providers, terraform_dir=".", max_workers=5):
    """
    Install multiple Terraform providers concurrently
    
    Args:
        providers (list): List of provider configurations, each containing:
                         - download_url (dict): Platform-specific URLs
                         - source (str): Provider source path (alias is last part after /)
                         - version (str): Provider version
        terraform_dir (str): Terraform directory path
        max_workers (int): Maximum concurrent downloads
    
    Returns:
        dict: Results for each provider installation
    """
    results = {}
    terraform_dir = Path(terraform_dir)
    
    if not terraform_dir.exists():
        terraform_dir.mkdir(parents=True, exist_ok=True)
        print(f"Created terraform directory: {terraform_dir}")
    
    def install_single_provider(provider_config):
        """Helper function to install a single provider"""
        source = provider_config.get('source')
        version = provider_config.get('version')
        download_url = provider_config.get('download_url')
        
        try:
            print(f"\nInstalling {source} v{version}...")
            success = install_provider(
                download_url=download_url,
                source=source,
                version=version,
                terraform_dir=terraform_dir
            )
            return (source, version, success)
        except Exception as e:
            print(f"Error installing {source} v{version}: {e}")
            return (source, version, False)
    
    # Use ThreadPoolExecutor for concurrent downloads
    with concurrent.futures.ThreadPoolExecutor(max_workers=max_workers) as executor:
        futures = [executor.submit(install_single_provider, provider) for provider in providers]
        
        for future in concurrent.futures.as_completed(futures):
            source, version, success = future.result()
            results[f"{source}:{version}"] = success
    
    # Generate terraform RC file after all providers are installed
    generate_terraform_rc(terraform_dir)
    
    # Print summary
    print("\n=== Installation Summary ===")
    successful = sum(1 for v in results.values() if v)
    failed = sum(1 for v in results.values() if not v)
    print(f"Successful: {successful}, Failed: {failed}")
    
    if failed > 0:
        print("Failed providers:")
        for key, success in results.items():
            if not success:
                print(f"  - {key}")
    
    return results


def generate_terraform_rc(terraform_dir):
    """
    Generate a terraform CLI configuration file for local provider installation
    
    Args:
        terraform_dir (str): Terraform directory path
    """
    terraform_dir = Path(terraform_dir)
    rc_file_path = terraform_dir / "terraform.rc"
    plugins_path = terraform_dir / "terraform.d" / "plugins"
    
    # Build the configuration with wildcard to include all registries
    config_lines = [
        "provider_installation {",
        "  filesystem_mirror {",
        f'    path    = "{plugins_path.absolute()}"',
        '    include = ["*/*/*"]',
        "  }",
        "}"
    ]
    
    # Write the configuration file
    with open(rc_file_path, 'w') as f:
        f.write('\n'.join(config_lines))
    
    print(f"Generated terraform RC file at: {rc_file_path}")
    print(f"To use it, set: export TF_CLI_CONFIG_FILE=\"{rc_file_path.absolute()}\"")
    
    return rc_file_path


def parse_arguments():
    """Parse command line arguments when used as a standalone script"""
    parser = argparse.ArgumentParser(
        description="Download and install multiple Terraform providers with aliasing support"
    )
    
    parser.add_argument(
        "--providers-json",
        required=True,
        help="Path to JSON file containing array of provider configurations"
    )
    parser.add_argument(
        "--terraform-dir",
        default=".",
        help="Terraform directory path (default: current directory)"
    )
    parser.add_argument(
        "--max-workers",
        type=int,
        default=5,
        help="Maximum concurrent downloads (default: 5)"
    )
    
    return parser.parse_args()


def main():
    """Main entry point when used as a standalone script"""
    args = parse_arguments()
    
    # Load providers configuration from JSON file
    try:
        with open(args.providers_json, 'r') as f:
            providers_config = json.load(f)
    except FileNotFoundError:
        print(f"Error: File '{args.providers_json}' not found")
        sys.exit(1)
    except json.JSONDecodeError as e:
        print(f"Error: Invalid JSON in '{args.providers_json}': {e}")
        sys.exit(1)
    
    # Validate the JSON structure
    if not isinstance(providers_config, list):
        print("Error: JSON file must contain an array of provider configurations")
        sys.exit(1)
    
    # Install all providers
    results = install_multiple_providers(
        providers=providers_config,
        terraform_dir=args.terraform_dir,
        max_workers=args.max_workers
    )
    
    # Exit with error if any provider failed
    if not all(results.values()):
        sys.exit(1)


if __name__ == "__main__":
    main()