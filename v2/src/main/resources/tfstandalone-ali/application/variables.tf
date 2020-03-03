variable "cluster" {
  type = object({
    name = string
    aliRegion = string
    azs = list(string)
    privateSubnetCIDR = list(string)
    publicSubnetCIDR  = list(string)
    vpcCIDR = string
    stackName = string
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

variable "kube_config_file_path" {
  type = string
}

variable "resources" {
  type = any
}

variable "cc_auth_token" {
  type = string
  default = "cc20deal"
}

variable "cc_host" {
  type = string
  default = "deployerdev.capillary.in"
}

variable "dev_mode" {
  type = bool
  default = false
}