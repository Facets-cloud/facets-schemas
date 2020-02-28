variable "cluster" {
  type = object({
    name = string
    aliRegion = string
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
      cluster_nodes = any
      security_group_id = string
      log_project_name = string
    })
    helm_details = object({
      tiller_sa = string
    })
  })
}