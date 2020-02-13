output "k8s_details" {
  value = {
    cluster_id = module.managed-k8s.this_k8s_id
    cluster_nodes = module.managed-k8s.this_k8s_nodes
    security_group_id = module.managed-k8s.this_security_group_id
    log_project_name = module.managed-k8s.this_sls_project_name
    helm_details = {
      tiller_sa = kubernetes_service_account.tiller.metadata[0].name
    }
  }
}

output "vpc_details" {
  value = {
    vpc_id = module.managed-k8s.this_vpc_id
    vswitch_ids = module.managed-k8s.this_vswitch_ids
  }
}