def createAWSProvider(awsregion) {
    sh """
    echo '''
    provider "kubernetes" {
        config_path = "$AWS_KUBECONFIG"
    }

    provider "helm" {
    kubernetes {
        config_path = "$AWS_KUBECONFIG"
    }
    }
    provider "aws" {
        alias  = "tooling"
        region = "$awsregion"
        access_key = "$AWS_ACCESS_KEY_ID"
        secret_key = "$AWS_SECRET_ACCESS_KEY"
    }
    provider "acme" {
        server_url = "https://acme-staging-v02.api.letsencrypt.org/directory"
    }
    provider "kubectl" {
        config_path    = "$AWS_KUBECONFIG"
    }
    terraform {
        required_providers {
            helm = {
                source = "hashicorp/helm"
            }
            kubernetes = {
                source = "hashicorp/kubernetes"
            }
            acme = {
                source = "vancluever/acme"
            }
            aws = {
                source                = "hashicorp/aws"
                configuration_aliases = [aws.tooling]
            }
            kubectl = {
                source  = "gavinbunney/kubectl"
                version = ">= 1.13.0"
            }
        }
    required_version = ">= 1.0"
    }


    ''' > ${WORKSPACE}/capillary-cloud-tf/modules/aws-provider.tf

    """
}

def createGCPProvider(awsregion, project, gcpregion) {
    sh """ 
    echo '''
    provider "kubernetes" {
        config_path = "$GCP_KUBECONFIG"
    }
    provider "helm" {
        kubernetes {
            config_path = "$GCP_KUBECONFIG"
            load_config_file = false
        }
        version = "1.3.2"
    }
    provider "kubectl" {
        config_path    = "$GCP_KUBECONFIG"
    }
    provider "google" {
        credentials = "$CREDENTIALS"
        project = "$project"
        region  = "$gcpregion"
    }
    provider "aws" {
        alias  = "tooling"
        region = "$awsregion"
        access_key = "$AWS_ACCESS_KEY_ID"
        secret_key = "$AWS_SECRET_ACCESS_KEY"
    }
    provider "acme" {
        server_url = "https://acme-staging-v02.api.letsencrypt.org/directory"
    }
    terraform {
        required_providers {
            helm = {
                source = "hashicorp/helm"
            }
            kubernetes = {
                source = "hashicorp/kubernetes"
            }
            acme = {
                source = "vancluever/acme"
            }
            aws = {
                source                = "hashicorp/aws"
                configuration_aliases = [aws.tooling]
            }
            kubectl = {
                source  = "gavinbunney/kubectl"
                version = ">= 1.13.0"
            }
        }
        required_version = ">= 1.0"
    }
    ''' > ${WORKSPACE}/capillary-cloud-tf/modules/gcp-provider.tf
    """
}

return this
