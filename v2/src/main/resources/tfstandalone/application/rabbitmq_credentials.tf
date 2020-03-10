locals {
  rabbitmq_users = {
    for i,j in local.instances:
      i => {for p in local.instances[i]["credentialRequests"]["queues"]["rabbitmq"]:
        p["environment"]["userName"] => "root"
      }
  }

  rabbitmq_passwords = {
    for i,j in local.instances:
      i => {for p in local.instances[i]["credentialRequests"]["queues"]["rabbitmq"]:
        p["environment"]["password"] => var.resources[p["resourceType"]][p["resourceName"]]["root_password"]
      }
  }
}