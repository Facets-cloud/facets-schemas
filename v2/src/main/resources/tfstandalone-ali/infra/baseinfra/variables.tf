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

variable "ec2_token_refresher_key_id" {
  type = string
}

variable "ec2_token_refresher_key_secret" {
  type = string
}

variable "tooling-vpc" {
  type = object({
    cidr = string
    private_subnet_id = string
    region = string
  })
}