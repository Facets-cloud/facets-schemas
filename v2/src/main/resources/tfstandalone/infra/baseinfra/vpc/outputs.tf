output "vpc_details" {
  value = {
    vpc_id = module.vpc.vpc_id
    private_subnets = module.vpc.private_subnets
    public_subnets = module.vpc.public_subnets
    private_route_table_ids = module.vpc.private_route_table_ids
  }
}