module "k8scluster" {
  source = "./managed_k8s_cluster"
  cluster = var.cluster
}

module "helm" {
  source = "./kube_helm_setup"
  cluster = var.cluster
  kube_config_file_path = module.k8scluster.kube_config_file_path
  k8s_details = module.k8scluster.k8s_details
  vpc_details = module.k8scluster.vpc_details
  ec2_token_refresher_key_id = var.ec2_token_refresher_key_id
  ec2_token_refresher_key_secret = var.ec2_token_refresher_key_secret
}