output "infra_details" {
  value = {
    base_infra_details = module.baseinfra.base_infra_details
    resources = {
      mysql = module.mysql.mysql_details
      s3 = module.oss.oss_details
    }
  }
}

output "kube_config_file_path" {
  value = module.baseinfra.kube_config_file_path
}
