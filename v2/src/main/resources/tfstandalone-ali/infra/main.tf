module "baseinfra" {
  source = "./baseinfra"
  cluster = var.cluster
}

//module "mysql" {
//  source = "./mysql"
//  baseinfra = module.baseinfra.base_infra_details
//  cluster = var.cluster
//}
//
//module "oss" {
//  source = "./oss"
//  baseinfra = module.baseinfra.base_infra_details
//  cluster = var.cluster
//}