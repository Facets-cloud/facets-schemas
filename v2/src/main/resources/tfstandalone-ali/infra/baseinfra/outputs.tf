//output "base_infra_details" {
//  value = {
//    vpc_details = module.k8scluster.vpc_details
//    k8s_details = module.k8scluster.k8s_details
//    helm_details = module.helm.helm_details
////    log-project-name = module.k8scluster.log-project-name
//  }
//}

output "vswitch_ids" {
  value = module.k8scluster.vswitch_ids
}

output "vpc_id" {
  value = module.k8scluster.vpc_id
}

output "log_project_name" {
  value = module.k8scluster.log_project_name
}

output "security_group_id" {
  value = module.k8scluster.security_group_id
}

output "cluster_nodes" {
  value = module.k8scluster.cluster_nodes
}

output "cluster_id" {
  value = module.k8scluster.cluster_id
}

output "kube_config_file_path" {
  value = "~/.captf/kube/ali/config"
}
output "tiller_sa" {
  value = module.helm.tiller_sa
}
//
//output "k8s-details" {
//  value = module.k8scluster.k8s_details
//}
//
//output "vpc-details" {
//  value = module.k8scluster.vpc_details
//}
//
//output "log-project-name" {
//  value = module.k8scluster.log-project-name
//}