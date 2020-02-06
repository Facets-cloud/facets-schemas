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
    })
  })
}

variable "resources" {
  type = any
}