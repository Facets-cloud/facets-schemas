locals {
  devModeCluster = {
    "aliRegion" = "ap-southeast-3",
    "name" = "ali-test-01",
    "azs" = [
      "ap-southeast-3a", "ap-southeast-3b"
    ],
    "privateSubnetCIDR" = [
      "10.100.100.0/24", "10.100.101.0/24"
    ],
    "publicSubnetCIDR" = [
      "10.100.110.0/24", "10.100.111.0/24"
    ],
    "vpcCIDR": "10.100.0.0/16",
    "stackName" : "test"
  }
  cluster = var.dev_mode == true ? local.devModeCluster : jsondecode(data.http.cluster[0].body)

  awsRegion = "us-east-1"
}

data "http" "cluster" {
  count = var.dev_mode ? 0 : 1
  url = "http://localhost:8080/internal/capillarycloud/api/mockcluster"
  # Optional request headers
  request_headers = {
    Accept = "application/json"
  }
}

provider "aws" {
  region = local.awsRegion
  version = "~> 2.45.0"
  profile = "tfmj"
}

provider "alicloud" {
  region     = local.cluster.aliRegion
  version = "1.71.1"
}

terraform {
  backend "s3" {
    bucket = "capillary-cloud-ali-tfstate"
    key    = "tfstate"
    region = "us-east-1"
    dynamodb_table = "capillary-cloud-ali-tflock"
  }
}

module "infra" {
  source = "./infra"
  cluster = local.cluster
  ec2_token_refresher_key_id = var.ali-ecr-token-refresher-key-id
  ec2_token_refresher_key_secret = var.ali-ecr-token-refresher-key-secret
}

module "application" {
  source = "./application"
  baseinfra = module.infra.infra_details.base_infra_details
  cluster = local.cluster
  resources = module.infra.infra_details.resources
  kube_config_file_path = module.infra.kube_config_file_path
  dev_mode = var.dev_mode
}

variable "ali-ecr-token-refresher-key-id" {
  type = string
}

variable "ali-ecr-token-refresher-key-secret" {
  type = string
}

variable "dev_mode" {
  type = bool
  default = false
}

output "mysq_connection_string" {
  value = module.infra.infra_details.resources.mysql
}