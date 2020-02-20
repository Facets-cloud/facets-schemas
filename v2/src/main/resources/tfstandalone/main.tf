locals {
//  cluster = jsondecode(data.http.cluster.body)
  devModeCluster = {
    "awsRegion" = "us-west-2",
    "azs" = [
      "us-west-2a", "us-west-2b"
    ],
    "name" = "test009",
    "privateSubnetCIDR" = [
      "10.200.100.0/24", "10.200.101.0/24"
    ],
    "publicSubnetCIDR" = [
      "10.200.110.0/24", "10.200.111.0/24"
    ],
    "vpcCIDR": "10.200.0.0/16",
    "roleARN": "arn:aws:iam::486456986266:role/capillary-cloud-freemium-role",
    "externalId": "123"
  }
  cluster = var.dev_mode == true ? local.devModeCluster : jsondecode(data.http.cluster[0].body)

}

provider "aws" {
  region = local.cluster.awsRegion
  version = "~> 2.45.0"
  assume_role {
    role_arn = local.cluster.roleARN
    session_name = "capillary-cloud-tf-${uuid()}"
    external_id = local.cluster.externalId
  }
}

terraform {
  backend "s3" {
    bucket = "capillary-cloud-tfstate"
    key    = "tfstate"
    region = "us-east-1"
    dynamodb_table = "capillary-cloud-tflock"
  }
}

module "infra" {
  source = "./infra"
  cluster = local.cluster
}

module "application" {
  source = "./application"
  baseinfra = module.infra.infra_details.base_infra_details
  cluster = local.cluster
  resources = module.infra.infra_details.resources
}

data "http" "cluster" {
  count = var.dev_mode ? 0 : 1
  request_headers = {
    Accept = "application/json"
    X-DEPLOYER-INTERNAL-AUTH-TOKEN = var.cc_auth_token
  }

  url = "https://${var.cc_host}/cc/v1/aws/clusters/${terraform.workspace}"
}

variable "cc_auth_token" {
  type = string
  default = "cc20deal"
}

variable "cc_host" {
  type = string
  default = "deployerdev.capillary.in"
}

variable "dev_mode" {
  type = bool
  default = false
}
