locals {
  instances = {
    "db1" = {
      name = "db1"
      k8s_service_names = [
        "mydb1",
        "yourdb1"]
    }
    "db2" = {
      name = "db2"
      k8s_service_names = [
        "mydb2",
        "yourdb2"]
    }
  }

  k8s_service_names_transpose = transpose({
  for i in values(local.instances):
  i["name"] => i["k8s_service_names"]
  })

  k8s_service_names_map = {
  for k, v in local.k8s_service_names_transpose:
  k => element(v, 0)
  }
}

resource "random_string" "root_password" {
  for_each = local.instances
  length = 12
  override_special = "#$!"
}

resource "alicloud_db_instance" "default" {
  for_each = local.instances
  engine = "MySQL"
  engine_version = "5.7"
  instance_type = "rds.mysql.t1.small"
  instance_storage = "20"
  instance_charge_type = "Postpaid"
  instance_name = "${var.cluster.name}${each.key}"
  vswitch_id = var.baseinfra.vpc_details.vswitch_ids[0]
  monitoring_period = "60"
}

resource "alicloud_db_account" "account" {
  for_each = local.instances
  instance_id = alicloud_db_instance.default[each.key].id
  name        = "root"
  password    = random_string.root_password[each.key].result
}

provider "kubernetes" {
  version = "~> 1.10"
  config_path = "~/.captf/kube/ali/config"
}

resource "kubernetes_service" "mysql-k8s-service" {
  for_each = local.k8s_service_names_map
  metadata {
    name = each.key
  }
  spec {
    type = "ExternalName"
    external_name = alicloud_db_instance.default[each.value].instance_name
  }
}