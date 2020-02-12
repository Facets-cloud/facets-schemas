variable "cluster" {
  type = object({
    name = string
    awsRegion = string
    azs = list(string)
    privateSubnetCIDR = list(string)
    publicSubnetCIDR  = list(string)
    vpcCIDR = string
    iamRole = string
    externalId = string
  })
}