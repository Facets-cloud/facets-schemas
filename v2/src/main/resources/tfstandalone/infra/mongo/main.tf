locals {
  instances = {
    "db1" = {
      name = "mongo1"
      k8s_service_names = ["mymongo1", "yourmongo1"]
      data_volume_size = 10
      size = "SMALL"
    }
    "db2" = {
      name = "mongo2"
      k8s_service_names = ["mymongo2", "yourmongo2"]
      data_volume_size = 15
      size = "SMALL"
    }
  }

  sizings = {
    SMALL = {
      cpu = 1
      memory = "1500Mi"
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

provider "helm" {
  kubernetes {
    host = var.baseinfra.k8s_details.auth.host
    cluster_ca_certificate = var.baseinfra.k8s_details.auth.cluster_ca_certificate
    token = var.baseinfra.k8s_details.auth.token
    load_config_file = false
  }
  version = "~> 0.10.4"
  service_account = var.baseinfra.k8s_details.helm_details.tiller_sa
  install_tiller = true
}

data "helm_repository" "stable" {
  name = "stable"
  url  = "https://kubernetes-charts.storage.googleapis.com"
}

resource "random_string" "root_password" {
  for_each = local.instances
  length = 12
  override_special = "#$!"
}

resource helm_release "mongo" {
  for_each = local.instances
  name       = "mongo-rs-${each.key}"
  repository = data.helm_repository.stable.metadata[0].name
  chart      = "mongodb-replicaset"
  version    = "3.11.5"

  set_string {
    name = "persistentVolume.storageClass"
    value = "gp2"
  }

  set_string {
    name = "persistentVolume.size"
    value = "${each.value["data_volume_size"]}Gi"
  }

  set_string {
    name = "auth.adminUser"
    value = "root"
  }

  set_string {
    name = "auth.adminPassword"
    value = random_string.root_password[each.key].result
  }

  values = [
<<RESOURCES
resources:
  requests:
    cpu: ${local.sizings[each.value["size"]]["cpu"]}
    memory: ${local.sizings[each.value["size"]]["memory"]}
RESOURCES
  ]

}

provider "kubernetes" {
  host                   = var.baseinfra.k8s_details.auth.host
  cluster_ca_certificate = var.baseinfra.k8s_details.auth.cluster_ca_certificate
  token                  = var.baseinfra.k8s_details.auth.token
  load_config_file       = false
  version                = "~> 1.10"
}

//resource "kubernetes_service" "mysql-k8s-service" {
//  for_each = local.k8s_service_names_map
//  metadata {
//    name = each.key
//  }
//  spec {
//    type = "ExternalName"
//    external_name = aws_db_instance.rds-instance[each.value].address
//  }
//}

