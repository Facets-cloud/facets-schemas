module "baseinfra" {
  source = "./baseinfra"
  cluster = var.cluster
  ec2_token_refresher_key_id = var.ec2_token_refresher_key_id
  ec2_token_refresher_key_secret = var.ec2_token_refresher_key_secret
}

module "mysql" {
  source = "./mysql"
  baseinfra = module.baseinfra.base_infra_details
  cluster = var.cluster
}

module "oss" {
  source = "./oss"
  cluster = var.cluster
  baseinfra = module.baseinfra.base_infra_details
}