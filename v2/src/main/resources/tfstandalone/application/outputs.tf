output "test" {
  value = {
    for i, j in local.instances:
      i => local.mongo_passwords[i]
  }
}

output "test2" {
  value = {
  for i, j in local.instances:
    i => merge(local.dynamic_environment_variables_map[i], j["environmentVariables"]["static"])
  }
}