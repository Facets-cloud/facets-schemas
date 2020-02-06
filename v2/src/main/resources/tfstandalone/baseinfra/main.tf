module "vpc" {
  source = "./vpc"
  cluster = var.cluster
}

module "k8scluster" {
  source = "./k8s_cluster"
  cluster = var.cluster
  vpc_details = module.vpc.vpc_details
}