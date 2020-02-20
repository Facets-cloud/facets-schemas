module "k8scluster" {
  source = "./managed_k8s_cluster"
  cluster = var.cluster
}

module "helm" {
  source = "./kube_helm_setup"
  cluster = var.cluster
  kube_config_file_path = module.k8scluster.kube_config_file_path
}