locals {
  mongo_users = {
    for i,j in local.json_data["instances"]:
      i => {for p in local.json_data["instances"][i]["credentialRequests"]["queues"]["rabbitmq"]:
        p["environment"]["userName"] => "root"
      }
  }

  mongo_passwords = {
    for i,j in local.json_data["instances"]:
      i => {for p in local.json_data["instances"][i]["credentialRequests"]["queues"]["rabbitmq"]:
        p["environment"]["password"] => var.resources[p["resourceType"]][p["resourceName"]]["root_password"]
      }
  }
}