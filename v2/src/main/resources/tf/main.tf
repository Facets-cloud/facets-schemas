provider "aws" {
  region = var.awsRegion
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

module "crm-vpc" {
  source = "./vpc"
  name = var.name
  infraResourceName = "crm-vpc"
  awsRegion = var.awsRegion
  azs = var.azs
  privateSubnetCIDR = var.privateSubnetCIDR
  publicSubnetCIDR = var.publicSubnetCIDR
  vpcCIDR = var.vpcCIDR
}

module "crm-k8scluster" {
  source = "./k8s_cluster"
  name = var.name
  subnets = module.crm-vpc.private_subnets
  vpc_id = module.crm-vpc.vpc_id
}

provider "restapi" {
  uri                  = "http://127.0.0.1:8080/"
  debug                = true
  write_returns_object = true
  headers              = {
    "X-DEPLOYER-INTERNAL-AUTH-TOKEN" = "${var.apiToken}"
  }
  update_method        = "POST"
}
