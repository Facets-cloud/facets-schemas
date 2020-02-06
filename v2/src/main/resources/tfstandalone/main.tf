locals {
  cluster = jsondecode(data.http.example.body)
}

provider "aws" {
  region = local.cluster.awsRegion
  profile = "tfmj"
  version = "~> 2.45.0"
}

terraform {
  backend "s3" {
    bucket = "capillary-cloud-tfstate"
    key    = "tfstate"
    region = "us-east-1"
    profile = "tfmj"
    dynamodb_table = "capillary-cloud-tflock"
  }
}

module "baseinfra" {
  source = "./baseinfra"
  cluster = local.cluster
}

module "mysql" {
  source = "./mysql"
  baseinfra = module.baseinfra.base_infra_details
  cluster = local.cluster
}

module "application" {
  source = "./application"
  baseinfra = module.baseinfra.base_infra_details
  cluster = local.cluster
  resources = {
    "mysql" = module.mysql.mysql_details
  }
}

data "http" "example" {
  url = "http://localhost:8080/internal/capillarycloud/api/mockcluster"
  # Optional request headers
  request_headers = {
    Accept = "application/json"
  }
}