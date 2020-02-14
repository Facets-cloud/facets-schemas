//module "vpc" {
//  source = "vpc"
//  cluster = var.cluster
//}

module "helm" {
  source = "./kube_helm_setup"
  cluster = var.cluster
  kubernetes_cluster_id = module.k8scluster.k8s_details.cluster_id
}

module "k8scluster" {
  source = "./managed_k8s_cluster"
  cluster = var.cluster
}