locals {
  stackName = var.cluster.stackName
  definitions = fileset("../stacks/${local.stackName}/zookeeper/instances", "*.json")
  instances = {
  for def in local.definitions:
  replace(def, ".json", "") => jsondecode(file("../stacks/${local.stackName}/zookeeper/instances/${def}"))
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

data "helm_repository" "incubator" {
  name = "incubator"
  url  = "http://storage.googleapis.com/kubernetes-charts-incubator"
}

resource helm_release "zookeeper" {
  for_each = local.instances
  name       = "zk-${each.key}"
  repository = data.helm_repository.incubator.metadata[0].name
  chart      = "zookeeper"
  version    = "2.1.3"
  timeout    = 900

  set_string {
    name = "image.tag"
    value = "3.4"
  }
}

provider "kubernetes" {
  host                   = var.baseinfra.k8s_details.auth.host
  cluster_ca_certificate = var.baseinfra.k8s_details.auth.cluster_ca_certificate
  token                  = var.baseinfra.k8s_details.auth.token
  load_config_file       = false
  version                = "~> 1.10"
}

resource "kubernetes_service" "mongo-k8s-service" {
  for_each = local.k8s_service_names_map
  metadata {
    name = each.key
  }
  spec {
    type = "ExternalName"
    external_name = "zk-${each.key}.default.svc.cluster.local"
  }
}