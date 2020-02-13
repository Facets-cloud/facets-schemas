variable "cluster" {
  type = object({
    name = string
    awsRegion = string
    azs = list(string)
    privateSubnetCIDR = list(string)
    publicSubnetCIDR  = list(string)
    vpcCIDR = string
  })
}

variable "baseinfra" {
  type = object({
    vpc_details = object({
      vpc_id = string
      vswitch_ids = list(string)
    })
    k8s_details = object({
      cluster_id = string
      cluster_nodes = list(string)
      security_group_id = list(string)
      log_project_name = string
    })
  })
}