//module "vpc" {
//  source = "vpc"
//  cluster = var.cluster
//}

module "k8scluster" {
  source = "managed_k8s_cluster"
  cluster = var.cluster
}