//provider "aws" {
//  region = var.tooling-vpc.region
//  version = "~> 2.45.0"
//  profile = "tfmj"
//}
//
//provider "alicloud" {
//  region     = var.cluster.aliRegion
//  version = "1.71.1"
//}

data "aws_route_table" "aws_private_subnet_rt" {
  subnet_id = var.tooling-vpc.private_subnet_id
}

resource "alicloud_vpn_gateway" "aws_vpn_gateway" {
  name                 = "AWS-VPN-Gateway"
  vpc_id               = var.vpc_details.vpc_id
  bandwidth            = "10"
  enable_ssl           = false
  instance_charge_type = "PostPaid"
  description          = "AWS-VPN-Gateway"
  vswitch_id           = var.vpc_details.vswitch_ids[0]
}

resource "aws_vpn_gateway" "vpn_gw" {
  vpc_id = "vpc-b1e65fd4"

  tags = {
    Name = "AWS-VPN-GW"
  }
}

resource "aws_customer_gateway" "alicloud_vpn_gw" {
  bgp_asn    = 65000
  ip_address = alicloud_vpn_gateway.aws_vpn_gateway.internet_ip
  type       = "ipsec.1"

  tags = {
    Name = "alicloud-customer-gateway"
  }
}

resource "aws_vpn_connection" "alicloud_vpn_connection" {
  vpn_gateway_id      = aws_vpn_gateway.vpn_gw.id
  customer_gateway_id = aws_customer_gateway.alicloud_vpn_gw.id
  type                = "ipsec.1"
  static_routes_only = true
}

resource "aws_vpn_connection_route" "alicloud" {
  destination_cidr_block = var.cluster.vpcCIDR
  vpn_connection_id      = aws_vpn_connection.alicloud_vpn_connection.id
}

resource "alicloud_vpn_customer_gateway" "aws_customer_gw_1" {
  name        = "AWSCustomerGateway1"
  ip_address  = aws_vpn_connection.alicloud_vpn_connection.tunnel1_address
  description = "AWSCustomerGateway1"
}

resource "alicloud_vpn_connection" "ipsec_connection_1" {
  name                = "IPSecConnection1"
  vpn_gateway_id      = alicloud_vpn_gateway.aws_vpn_gateway.id
  customer_gateway_id = alicloud_vpn_customer_gateway.aws_customer_gw_1.id
  local_subnet        = [var.cluster.vpcCIDR]
  remote_subnet       = [var.tooling-vpc.cidr]
  effect_immediately  = true
  ike_config {
    ike_auth_alg  = "sha1"
    ike_enc_alg   = "aes"
    ike_version   = "ikev1"
    ike_mode      = "main"
    ike_lifetime  = 86400
    psk           = aws_vpn_connection.alicloud_vpn_connection.tunnel1_preshared_key
    ike_pfs       = "group2"
    ike_local_id = alicloud_vpn_gateway.aws_vpn_gateway.internet_ip
    ike_remote_id = aws_vpn_connection.alicloud_vpn_connection.tunnel1_address
  }
  ipsec_config {
    ipsec_pfs      = "group2"
    ipsec_enc_alg  = "aes"
    ipsec_auth_alg = "sha1"
    ipsec_lifetime = 86400
  }
}

resource "alicloud_vpn_customer_gateway" "aws_customer_gw_2" {
  name        = "AWSCustomerGateway2"
  ip_address  = aws_vpn_connection.alicloud_vpn_connection.tunnel2_address
  description = "AWSCustomerGateway2"
}

resource "alicloud_vpn_connection" "ipsec_connection_2" {
  name                = "IPSecConnection2"
  vpn_gateway_id      = alicloud_vpn_gateway.aws_vpn_gateway.id
  customer_gateway_id = alicloud_vpn_customer_gateway.aws_customer_gw_2.id
  local_subnet        = [var.cluster.vpcCIDR]
  remote_subnet       = [var.tooling-vpc.cidr]
  effect_immediately  = true
  ike_config {
    ike_auth_alg  = "sha1"
    ike_enc_alg   = "aes"
    ike_version   = "ikev1"
    ike_mode      = "main"
    ike_lifetime  = 86400
    psk           = aws_vpn_connection.alicloud_vpn_connection.tunnel2_preshared_key
    ike_pfs       = "group2"
    ike_local_id = alicloud_vpn_gateway.aws_vpn_gateway.internet_ip
    ike_remote_id = aws_vpn_connection.alicloud_vpn_connection.tunnel2_address
  }
  ipsec_config {
    ipsec_pfs      = "group2"
    ipsec_enc_alg  = "aes"
    ipsec_auth_alg = "sha1"
    ipsec_lifetime = 86400
  }
}

resource "alicloud_vpn_route_entry" "alicloud_vpn_route_entry_1" {
  vpn_gateway_id = alicloud_vpn_gateway.aws_vpn_gateway.id
  route_dest     = var.tooling-vpc.cidr
  next_hop       = alicloud_vpn_connection.ipsec_connection_1.id
  weight         = 0
  publish_vpc    = true
}

resource "alicloud_vpn_route_entry" "alicloud_vpn_route_entry_2" {
  vpn_gateway_id = alicloud_vpn_gateway.aws_vpn_gateway.id
  route_dest     = var.tooling-vpc.cidr
  next_hop       = alicloud_vpn_connection.ipsec_connection_2.id
  weight         = 100
  publish_vpc    = true
}

resource "aws_route" "r" {
  route_table_id            = data.aws_route_table.aws_private_subnet_rt.id
  destination_cidr_block    = var.cluster.vpcCIDR
  gateway_id = aws_vpn_gateway.vpn_gw.id
}