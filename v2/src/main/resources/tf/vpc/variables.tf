variable "name" {}

variable "clusterId" {}

variable "infraResourceName" {}

variable "vpcCIDR" {}
variable "azs" {
  type = list
}
variable "publicSubnetCIDR" {
  type = list
}
variable "privateSubnetCIDR" {
  type = list
}

variable "awsRegion" {}