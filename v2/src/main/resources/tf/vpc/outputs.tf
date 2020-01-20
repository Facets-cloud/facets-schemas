output "private_subnets" {
  value = "${module.vpc.private_subnets}"
}

output "vpc_id" {
  value = "${module.vpc.vpc_id}"
}