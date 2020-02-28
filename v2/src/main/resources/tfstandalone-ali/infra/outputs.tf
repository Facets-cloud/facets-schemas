output "infra_details" {
  value = {
    base_infra_details = module.baseinfra.base_infra_details
    resources = {
      mysql = module.mysql.mysql_details
      oss = module.oss.oss_details
    }
  }
}
