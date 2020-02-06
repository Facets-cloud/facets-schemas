module "vpc" {
  source = "terraform-aws-modules/vpc/aws"

  name = "${var.cluster.name}-vpc"
  cidr = var.cluster.vpcCIDR

  azs             = var.cluster.azs
  private_subnets = var.cluster.privateSubnetCIDR
  public_subnets  = var.cluster.publicSubnetCIDR

  enable_nat_gateway = true

  tags = {
    Terraform = "true",
    "kubernetes.io/cluster/${var.cluster.name}-k8s-cluster" = "shared"
  }
  version = "2.23.0"
}

//resource "restapi_object" "vpc_instance" {
//  path = "/internal/capillarycloud/api/infraResources/vpcs/crm-vpc/instances/${var.name}"
//  read_path = "/internal/capillarycloud/api/infraResources/vpcs/crm-vpc/instances/${var.name}"
//  destroy_path = "/internal/capillarycloud/api/infraResources/vpcs/crm-vpc/instances/${var.name}"
//  update_path = "/internal/capillarycloud/api/infraResources/vpcs/crm-vpc/instances/${var.name}"
//
//  data = <<EOF
//{
//  "privateSubnets": ${jsonencode(var.privateSubnetCIDR)},
//  "publicSubnets": ${jsonencode(var.publicSubnetCIDR)},
//  "vpcCIDR": "${var.vpcCIDR}",
//  "vpcId": "${module.vpc.vpc_id}"
//}
//EOF
//}
