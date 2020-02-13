provider "kubernetes" {
  version = "~> 1.10"
  config_path = "~/.captf/kube/ali/config"
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
      "iam_credential_requests" = [
        {
          resource_type = "s3"
          resource_name = "bucket1"
          permission = "READ_WRITE"
        }
      ]
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
      "iam_credential_requests" = [
        {
          resource_type = "oss"
          resource_name = "bucket1"
          permission = "READ_ONLY"
        }
      ]
      "mysql_credential_requests" = [
        {
          resource_type = "mysql"
          resource_name = "db1"
          permission = "READ_WRITE"
        }
      ]
    }
  }

  dynamic_environment_variables_map = {
  for i, j in local.instances:
  i => {
  for k, v in j["dynamic_environment_variables"]:
  k => var.resources[v["resource_type"]][v["resource_name"]][v["attribute"]]
  }
  }

  policy_attachments_map = {for k in flatten([
  for i, j in local.instances: [
  for p in j["iam_credential_requests"]: {
    iam_policy_name = var.resources[p["resource_type"]][p["resource_name"]]["iam_policies"][p["permission"]].name
    iam_policy_type = var.resources[p["resource_type"]][p["resource_name"]]["iam_policies"][p["permission"]].type
    application_name = i
  }
  ]]):
  "${k["iam_policy_name"]}-${k["application_name"]}" => k
  }
}

provider "helm" {
  kubernetes {
    config_path = "~/.captf/kube/ali/config"
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
    }),
    <<ROLE
roleName: ${alicloud_ram_role.application-role[each.key].arn}
ROLE
  ]
}

resource "alicloud_ram_role" "application-role" {
  for_each = local.instances
  name = "${var.cluster.name}-${each.key}-application-role"
  description = "${var.cluster.name}-${each.key}-application-role"
  document = <<EOF
  {
    "Statement": [
      {
        "Action": "sts:AssumeRole",
        "Effect": "Allow",
        "Principal": {
          "Service": [
            "ecs.aliyuncs.com"
          ]
        }
      },
      {
        "Action": "sts:AssumeRole",
        "Effect": "Allow",
        "Principal": {
            "RAM": [
                "${var.baseinfra.k8s_details.node_group_iam_role_arn}"
            ]
        }
      }
    ],
    "Version": "1"
  }
  EOF
}

resource "alicloud_ram_role_policy_attachment" "policy-attach" {
  for_each = local.policy_attachments_map
  policy_name = each.value["iam_policy_name"]
  policy_type = each.value["iam_policy_type"]
  role_name = alicloud_ram_role.application-role[each.value["application_name"]].name
}