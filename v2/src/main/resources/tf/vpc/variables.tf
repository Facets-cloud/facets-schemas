variable "name" {}

variable "infraResourceName" {}

variable "vpcCIDR" {}
variable "azs" {
  type = list(string)
}
variable "publicSubnetCIDR" {
  type = list(string)
}
variable "privateSubnetCIDR" {
  type = list(string)
}

variable "awsRegion" {}