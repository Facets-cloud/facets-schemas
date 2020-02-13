locals {
  // cluster = jsondecode(data.http.example.body)
  cluster = {
    "aliRegion" = "cn-huhehaote",
    "name" = "ali-test-01",
    "azs" = [
      "cn-huhehaote-a", "cn-huhehaote-b"
    ],
    "privateSubnetCIDR" = [
      "10.100.100.0/24", "10.100.101.0/24"
    ],
    "publicSubnetCIDR" = [
      "10.100.110.0/24", "10.100.111.0/24"
    ],
    "vpcCIDR": "10.100.0.0/16",
  }
}

//data "http" "example" {
//  url = "http://localhost:8080/internal/capillarycloud/api/mockcluster"
//  # Optional request headers
//  request_headers = {
//    Accept = "application/json"
//  }
//}

provider "alicloud" {
  region     = local.cluster.aliRegion
  version = "1.70.3"
}

terraform {
  backend "oss" {
    bucket = "capillary-cloud-ali-tfstate"
    prefix   = "cap-ali"
    key   = "tfstate"
    region = "cn-huhehaote"
    tablestore_endpoint = "https://terraform-remote.cn-huhehaote.ots.aliyuncs.com"
    tablestore_table = "capillary-cloud-ali-tflock"
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