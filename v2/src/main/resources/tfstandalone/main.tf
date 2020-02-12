locals {
  // cluster = jsondecode(data.http.example.body)
  cluster = {
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
    "iamRole": "arn:aws:iam::486456986266:role/capillary-cloud-freemium-role",
    "externalId": "123"
  }
}

provider "aws" {
  region = local.cluster.awsRegion
  version = "~> 2.45.0"
  assume_role {
    role_arn = local.cluster.iamRole
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


