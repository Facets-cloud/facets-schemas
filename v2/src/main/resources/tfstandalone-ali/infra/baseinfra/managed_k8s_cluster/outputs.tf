//output "k8s_details" {
//  value = {
//    cluster_id = module.managed-k8s.this_k8s_id
//    cluster_nodes = module.managed-k8s.this_k8s_nodes
//    security_group_id = module.managed-k8s.this_security_group_id
//    log_project_name = module.managed-k8s.this_sls_project_name
//  }
//}
//
//output "vpc_details" {
//  value = {
//    vpc_id = module.managed-k8s.this_vpc_id
//    vswitch_ids = module.managed-k8s.this_vswitch_ids
//  }
//}

output "vswitch_ids" {
  value = module.managed-k8s.this_vswitch_ids
}

output "vpc_id" {
  value = module.managed-k8s.this_vpc_id
}

output "log_project_name" {
  value = module.managed-k8s.this_sls_project_name
}

output "security_group_id" {
  value = module.managed-k8s.this_security_group_id
}

output "cluster_nodes" {
  value = module.managed-k8s.this_k8s_nodes
}

output "cluster_id" {
  value = module.managed-k8s.this_k8s_id
}

output "kube_config_file_path" {
  value = "~/.captf/kube/ali/config"
}

//
//output "log-project-name" {
//  value = module.managed-k8s.this_sls_project_name
//}