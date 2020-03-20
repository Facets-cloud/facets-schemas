output "mysql_details" {
  value = {
    for i in keys(local.instances):
    i => {
      original_endpoint: alicloud_db_instance.default[i].connection_string
      k8s_endpoints: local.instances[i]["k8s_service_names"]
      root_password: alicloud_db_account.account[i].password
      mysql_passwords: random_string.root_password[i].result
//      k8s_service_host = kubernetes_service.mysql-k8s-service[i].hostname
      grants: {
        READ_ONLY: ["SELECT"]
        READ_WRITE: ["SELECT", "INSERT", "UPDATE"]
      }
    }
  }
}