provider "aws" {
  alias = "peer"
  region = "us-west-1"
  version = "~> 2.45.0"
}

data "aws_caller_identity" "peer" {
  provider = aws.peer
}

# Requester's side of the connection.
resource "aws_vpc_peering_connection" "peer" {
  vpc_id        = var.vpc_details.vpc_id
  peer_vpc_id   = "vpc-b1e65fd4"
  peer_owner_id =  data.aws_caller_identity.peer.account_id
  peer_region   = "us-west-1"
  auto_accept   = false

  tags = {
    Side = "Requester"
  }
}

# Accepter's side of the connection.
resource "aws_vpc_peering_connection_accepter" "peer" {
  provider                  = aws.peer
  vpc_peering_connection_id = aws_vpc_peering_connection.peer.id
  auto_accept               = true

  tags = {
    Side = "Accepter"
  }
}

resource "aws_route" "peer_to_private_vpc" {
  provider = aws.peer
  route_table_id            = "rtb-4f79ae29"
  destination_cidr_block    = var.cluster.vpcCIDR
  vpc_peering_connection_id = aws_vpc_peering_connection.peer.id
}

resource "aws_route" "vpc_to_peer_pvt" {
  count = length(var.vpc_details.private_route_table_ids)
  route_table_id            = element(var.vpc_details.private_route_table_ids, count.index)
  destination_cidr_block    = "172.31.0.0/16"
  vpc_peering_connection_id = aws_vpc_peering_connection.peer.id
}