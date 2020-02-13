output "vpc_details" {
  value = {
    vpc_id = module.vpc.this_vpc_id
    vswitch_ids = module.vpc.this_vswitch_ids
  }
}