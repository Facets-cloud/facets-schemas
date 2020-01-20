provider "aws" {
  region = var.awsRegion
  profile = "tfmj"
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
  clusterId = var.id
  infraResourceName = "crm-vpc"
  awsRegion = var.awsRegion
  azs = var.azs
  privateSubnetCIDR = var.privateSubnetCIDR
  publicSubnetCIDR = var.publicSubnetCIDR
  vpcCIDR = var.vpcCIDR
}

module "k8s-cluster" {
  source          = "terraform-aws-modules/eks/aws"
  cluster_name    = "${var.name}-k8s-cluster"
  cluster_version = "1.14"
  subnets         = "${module.vpc.private_subnets}"
  vpc_id          = "${module.vpc.vpc_id}"
  manage_aws_auth = false
  worker_groups = [
    {
      instance_type = "m4.large"
      asg_max_size  = 2
    }
  ]
}

provider "kubernetes" {
  config_path = module.k8s-cluster.kubeconfig_filename == null ? module.k8s-cluster.kubeconfig_filename : "/nofile"
  load_config_file = false
}

provider "helm" {
  kubernetes {
    config_path = module.k8s-cluster.kubeconfig_filename == null ? module.k8s-cluster.kubeconfig_filename : "/nofile"
  }
}

provider "restapi" {
  uri                  = "http://127.0.0.1:8080/"
  debug                = true
  write_returns_object = true
  headers              = {
    "X-DEPLOYER-INTERNAL-AUTH-TOKEN" = "${var.apiToken}"
  }
}
