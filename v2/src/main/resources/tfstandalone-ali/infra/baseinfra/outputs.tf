output "base_infra_details" {
  value = {
    vpc_details = module.k8scluster.vpc_details
    k8s_details = module.k8scluster.k8s_details
  }
}