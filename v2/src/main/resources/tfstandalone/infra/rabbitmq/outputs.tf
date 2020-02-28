output "rabbitmq_details" {
  value = {
  for i in keys(local.instances):
    i => {
      root_password: random_string.root_password[i].result
    }
  }
}