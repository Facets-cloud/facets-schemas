locals {
  mongo_users = {
    for i,j in local.instances:
      i => {for p in local.instances[i]["credentialRequests"]["dbs"]["mongo"]:
        p["environment"]["userName"] => "root"
      }
  }

  mongo_passwords = {
    for i,j in local.instances:
      i => {for p in local.instances[i]["credentialRequests"]["dbs"]["mongo"]:
        p["environment"]["password"] => var.resources[p["resourceType"]][p["resourceName"]]["root_password"]
      }
  }
}