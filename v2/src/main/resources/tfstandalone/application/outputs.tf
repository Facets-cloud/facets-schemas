output "test" {
  value = {
    for i, j in local.json_data["instances"]:
      i => merge(local.mysql_users[i], local.mysql_passwords[i])
  }
}

output "test2" {
  value = {
  for i, j in local.json_data["instances"]:
    i => merge(local.dynamic_environment_variables_map[i], j["environmentVariables"]["static"])
  }
}