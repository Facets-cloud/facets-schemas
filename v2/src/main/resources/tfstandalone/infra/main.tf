module "baseinfra" {
  source = "./baseinfra"
  cluster = var.cluster
}

module "mysql" {
  source = "./mysql"
  baseinfra = module.baseinfra.base_infra_details
  cluster = var.cluster
}

module "s3" {
  source = "./s3"
  baseinfra = module.baseinfra.base_infra_details
  cluster = var.cluster
}

module "redis" {
  source = "./redis"
  baseinfra = module.baseinfra.base_infra_details
  cluster = var.cluster
}

module "mongo" {
  source = "./mongo"
  baseinfra = module.baseinfra.base_infra_details
  cluster = var.cluster
}