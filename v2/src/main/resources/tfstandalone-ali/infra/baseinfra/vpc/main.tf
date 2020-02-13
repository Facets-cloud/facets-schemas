module "vpc" {
  source = "alibaba/vpc/alicloud"
  region = var.cluster.aliRegion

  create = true
  vpc_name = "${var.cluster.name}-vpc"
  vpc_cidr = var.cluster.vpcCIDR

  availability_zones = var.cluster.azs
  vswitch_cidrs = [
    var.cluster.privateSubnetCIDR,
    var.cluster.publicSubnetCIDR]
  use_num_suffix = true
  vswitch_name = "tfvswitch"

  vpc_tags = {
    Terraform = "true",
    "kubernetes.io/cluster/${var.cluster.name}-k8s-cluster" = "shared"
  }

  vswitch_tags = {
    Terraform = "true",
    Endpoint = "true"
  }
}

//resource "alicloud_nat_gateway" "default" {
//  vpc_id = module.vpc.vpc_id
//  specification = "Small"
//  name = "tfnat"
//}
//
//resource "alicloud_eip" "default" {
//  count = 2
//  name = "tfnat-eip"
//}
//
//resource "alicloud_eip_association" "default" {
//  for_each = toset(alicloud_eip.default.*.id)
//  allocation_id = each.key
//  instance_id = alicloud_nat_gateway.default.id
//}
//
//resource "alicloud_common_bandwidth_package" "default" {
//  name = "tf_cbp"
//  bandwidth = 10
//  internet_charge_type = "PayByTraffic"
//  ratio = 100
//}
//
//resource "alicloud_common_bandwidth_package_attachment" "default" {
//  count = 2
//  bandwidth_package_id = alicloud_common_bandwidth_package.default.id
//  instance_id = element(alicloud_eip.default.*.id, count.index)
//}
//
//resource "alicloud_snat_entry" "default" {
//  depends_on = [
//    alicloud_eip_association.default]
//  snat_table_id = alicloud_nat_gateway.default.snat_table_ids
//  source_vswitch_id = module.vpc.this_vswitch_ids[0]
//  snat_ip = join(",", alicloud_eip.default.*.ip_address)
//}