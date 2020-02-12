output "infra_details" {
  value = {
    base_infra_details = module.baseinfra.base_infra_details
    resources = {
      mysql = module.mysql.mysql_details
      s3 = module.s3.s3_details
    }
  }
}