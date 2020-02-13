output "mysql_details" {
  value = {
    for i in keys(local.instances):
    i => {
      original_endpoint: alicloud_db_instance.default[i].connection_string
      k8s_endpoints: local.instances[i]["k8s_service_names"]
      root_passowrd: alicloud_db_account.account[i].password
    }
  }
}