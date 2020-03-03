provider "kubernetes" {
  version = "~>1.10.0"
  config_path = var.kube_config_file_path
}

locals {
  stackName = var.cluster.stackName
  json_data = jsondecode(file("../stacks/${local.stackName}/application/application.json"))
  sizing_data = jsondecode(file("../stacks/${local.stackName}/application/sizing.json"))
  dev_mode_build_map = {
  for i, j in local.json_data["instances"]:
    i => "486456986266.dkr.ecr.us-west-1.amazonaws.com/ops/demoapiservice:101e298"
  }
  build_map = var.dev_mode == true ? local.dev_mode_build_map : data.http.build
  sizing_map = {
    for i, j in local.json_data["instances"]:
      i => local.sizing_data[j["size"]]
  }
  dynamic_environment_variables_map = {
    for i,j in local.json_data["instances"]:
    i => {
      for k,v in j["environmentVariables"]["dynamic"]:
        k => var.resources[v["resourceType"]][v["resourceName"]][v["attribute"]]
    }
  }

  policy_attachments_map = {for k in flatten([
    for i,j in local.json_data["instances"]: [
      for p in j["credentialRequests"]["cloud"]: {
        iamPolicy = var.resources[p["resourceType"]][p["resourceName"]]["iam_policies"][p["permission"]]
        applicationName = i
        key = "${i}-${p["resourceType"]}-${p["resourceName"]}-${p["permission"]}"
      }
  ]]):
    k.key => k
  }
}

provider "helm" {
  kubernetes {
    config_path = var.kube_config_file_path
  }
  version = "~> 0.10.4"
  service_account = var.baseinfra.helm_details.tiller_sa
  install_tiller = true
}

resource "helm_release" "application" {
  for_each = local.json_data["instances"]
  chart = "../charts/capillary-base-cc"
  name = each.key
  version = "0.1.1"
  values = [
    jsonencode(each.value),
    jsonencode({
      sizing = local.sizing_map[each.key]
//      credentials = merge(local.mysql_users[each.key], local.mysql_passwords[each.key], local.mongo_users[each.key], local.mongo_passwords[each.key])
      //credentials = {}
      hpa = each.value["scaling"]
      image = local.build_map[each.key]
      configurations = merge(local.dynamic_environment_variables_map[each.key], each.value["environmentVariables"]["static"])
    })
//    <<ROLE
//roleName: ${aws_iam_role.application-role[each.key].arn}
//ROLE
  ]
}

//resource "aws_iam_role" "application-role" {
//  for_each = local.json_data["instances"]
//  name = "${var.cluster.name}-${each.key}-application-role"
//
//  assume_role_policy = <<EOF
//{
//  "Version": "2012-10-17",
//  "Statement": [
//    {
//      "Action": "sts:AssumeRole",
//      "Principal": {
//        "Service": "ec2.amazonaws.com"
//      },
//      "Effect": "Allow",
//      "Sid": ""
//    },
//    {
//      "Sid": "",
//      "Effect": "Allow",
//      "Principal": {
//        "AWS": "${var.baseinfra.k8s_details.node_group_iam_role_arn}"
//      },
//      "Action": "sts:AssumeRole"
//    }
//  ]
//}
//EOF
//}
//
//resource "aws_iam_role_policy_attachment" "policy-attach" {
//  for_each = local.policy_attachments_map
//  role = aws_iam_role.application-role[each.value["applicationName"]].name
//  policy_arn = each.value["iamPolicy"]
//}

data "http" "build" {
  for_each = var.dev_mode ? {} :local.json_data["instances"]
  request_headers = {
    Accept = "application/json"
    X-DEPLOYER-INTERNAL-AUTH-TOKEN = var.cc_auth_token
  }
  url = var.dev_mode ? "http://${var.cc_host}/cc/v1/build/deployer/${each.value["build"]["id"]}?strategy=QA":"https://${var.cc_host}/cc/v1/build/deployer/${each.value["build"]["id"]}?strategy=QA"
}