output "mysql_details" {
  value = {
    for i in keys(aws_db_instance.rds-instance):
    i => {
      original_endpoint: aws_db_instance.rds-instance[i].address
      k8s_endpoints: local.instances[i]["k8s_service_names"]
      root_password: aws_db_instance.rds-instance[i].password
      grants: {
        READ_ONLY: ["SELECT"]
        READ_WRITE: ["SELECT", "INSERT", "UPDATE"]
      }
  }
  }
}