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

variable "vpc_details" {
  type = object({
    vpc_id = string
    vswitch_ids = list(string)
  })
}

variable "tooling-vpc" {
  type = object({
    cidr = string
    private_subnet_id = string
    region = string
  })
}