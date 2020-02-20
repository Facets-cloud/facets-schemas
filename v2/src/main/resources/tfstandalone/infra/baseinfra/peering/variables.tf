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

variable "vpc_details" {
  type = object({
    vpc_id = string
    private_subnets = list(string)
    private_route_table_ids = list(string)
  })
}