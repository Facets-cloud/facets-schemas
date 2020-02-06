provider "kubernetes" {
  host                   = var.baseinfra.k8s_details.auth.host
  cluster_ca_certificate = var.baseinfra.k8s_details.auth.cluster_ca_certificate
  token                  = var.baseinfra.k8s_details.auth.token
  load_config_file       = false
  version                = "~> 1.10"
}

locals {
  instances = {
    "demovisitorservice" = {
      "helm_values" = <<VALUES
hpaMaxReplicas: 1
livenessPort: 9922
livenessInitialDelay: 10
credentials: {}
configurations: {}
lbType: internal
hpaEnabled: 'true'
enableLivenessTCP: 'true'
ports:
- {name: http, containerPort: 9922, lbPort: 80}
deploymentStrategy: RollingUpdate
hpaMinReplicas: 1
livenessTimeout: 1
podMemoryLimit: 0.5
readinessFailureThreshold: 3
livenessFailureThreshold: 3
enableReadinessTCP: 'true'
deploymentId: abcd
readinessSuccessThreshold: 1
protocolGroup: tcp
elbIdleTimeoutSeconds: 300
hpaMetricThreshold: 60
readinessInitialDelay: 10
image: 486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/demovisitorservice:101e298
buildId: abcd
readinessPort: 9922
livenessPeriod: 30
readinessTimeout: 1
livenessSuccessThreshold: 1
podCPULimit: 0.5
secretFileMounts: []
readinessPeriod: 30
VALUES
      "dynamic_environment_variables" = {
        "DB1_RDS_ENDPOINT" = {
          resource_type = "mysql"
          resource_name = "db1"
          attribute = "original_endpoint"
        }
      }
    }
    "demoapiservice" = {
      "helm_values" = <<VALUES
hpaMaxReplicas: 1
livenessPort: 8080
livenessInitialDelay: 10
credentials: {}
configurations: {VISITOR_SERVICE: demovisitorservice.default}
lbType: external
hpaEnabled: 'true'
enableLivenessTCP: 'true'
ports:
- {name: http, containerPort: 8080, lbPort: 80}
deploymentStrategy: RollingUpdate
hpaMinReplicas: 1
livenessTimeout: 1
podMemoryLimit: 0.5
readinessFailureThreshold: 3
livenessFailureThreshold: 3
enableReadinessTCP: 'true'
deploymentId: abcd
readinessSuccessThreshold: 1
protocolGroup: tcp
elbIdleTimeoutSeconds: 300
hpaMetricThreshold: 60
readinessInitialDelay: 10
image: 486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/demoapiservice:101e298
buildId: abcd
readinessPort: 8080
livenessPeriod: 30
readinessTimeout: 1
livenessSuccessThreshold: 1
podCPULimit: 0.5
secretFileMounts: []
readinessPeriod: 30
VALUES
      "dynamic_environment_variables" = {
        "DB1_RDS_ROOTPASSWORD" = {
          resource_type = "mysql"
          resource_name = "db1"
          attribute = "root_passowrd"
        }
      }
    }
  }

  dynamic_environment_variables_map = {
    for i,j in local.instances:
    i => {
      for k,v in j["dynamic_environment_variables"]:
        k => var.resources[v["resource_type"]][v["resource_name"]][v["attribute"]]
    }
  }
}

provider "helm" {
  kubernetes {
    host                   = var.baseinfra.k8s_details.auth.host
    cluster_ca_certificate = var.baseinfra.k8s_details.auth.cluster_ca_certificate
    token                  = var.baseinfra.k8s_details.auth.token
    load_config_file       = false
  }
  version = "~> 0.10.4"
  service_account = var.baseinfra.k8s_details.helm_details.tiller_sa
  install_tiller = true
}

resource "helm_release" "application" {
  for_each = local.instances
  chart = "../charts/capillary-base"
  name = each.key

  values = [
    each.value["helm_values"],
    jsonencode({
      configurations = merge(local.dynamic_environment_variables_map[each.key], yamldecode(each.value["helm_values"])["configurations"])
    })
  ]
}
