variable "cluster" {
  type = object({
    name = string
    awsRegion = string
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
      private_subnets = list(string)
    })
    k8s_details = object({
      auth = object({
        host = string
        cluster_ca_certificate = string
        token = string
      })
      helm_details = object({
        tiller_sa = string
      })
      node_group_iam_role_arn = string
    })
  })
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