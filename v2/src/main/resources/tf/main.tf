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

module "vpc" {
  source = "./vpc"
  name = var.name
  infraResourceName = "crm-vpc"
  awsRegion = var.awsRegion
  azs = var.azs
  privateSubnetCIDR = var.privateSubnetCIDR
  publicSubnetCIDR = var.publicSubnetCIDR
  vpcCIDR = var.vpcCIDR
}

module "k8scluster" {
  source = "./k8s_cluster"
  name = var.name
  subnets = module.vpc.private_subnets
  vpc_id = module.vpc.vpc_id
  infraResourceName = "crm-k8scluster"
  awsRegion = var.awsRegion
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
