locals {
  permission_callsigns = {
    "READ_WRITE" = "rw",
    "READ_ONLY" = "ro"
  }

  mysql_credntial_requests = {for k in flatten([
    for i,j in local.instances: [
      for p in j["credentialRequests"]["dbs"]["mysql"]: {
        mysql_host = var.resources["mysql"][p["resourceName"]]["original_endpoint"]
        mysql_root_password = var.resources["mysql"][p["resourceName"]]["root_password"]
        mysql_grants = var.resources["mysql"][p["resourceName"]]["grants"][p["permission"]]
        applicationName = i
        key = "${i}-${p["resourceType"]}-${p["resourceName"]}-${p["permission"]}"
        user = lower("${i}-${local.permission_callsigns[p["permission"]]}")
      }
      ]]):
    k.key => k
  }

  mysql_users = {
    for i,j in local.instances:
      i => {for p in local.instances[i]["credentialRequests"]["dbs"]["mysql"]:
        p["environment"]["userName"] => mysql_user.mysql_user["${i}-${p["resourceType"]}-${p["resourceName"]}-${p["permission"]}"].user
      }
  }

  mysql_passwords = {
    for i,j in local.instances:
      i => {for p in local.instances[i]["credentialRequests"]["dbs"]["mysql"]:
        p["environment"]["password"] => random_string.random_password["${i}-${p["resourceType"]}-${p["resourceName"]}-${p["permission"]}"].result
      }
  }
}

provider "mysql" {
}

resource "random_string" "random_password" {
  for_each = local.mysql_credntial_requests
  length = 12
  override_special = "#$!"
}

resource "mysql_user" "mysql_user" {
  for_each           = local.mysql_credntial_requests
  user               = each.value["user"]
  host               = "%"
  override_endpoint  = each.value["mysql_host"]
  plaintext_password = random_string.random_password[each.key].result
  override_admin_username = "root"
  override_admin_password = each.value["mysql_root_password"]
}

resource "mysql_grant" "mysql_grants" {
  for_each           = local.mysql_credntial_requests
  user       = mysql_user.mysql_user[each.key].user
  host       = mysql_user.mysql_user[each.key].host
  database   = "*"
  privileges = each.value["mysql_grants"]
  override_endpoint  = each.value["mysql_host"]
  override_admin_username = "root"
  override_admin_password = each.value["mysql_root_password"]
}