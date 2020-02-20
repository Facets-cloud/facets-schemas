module "baseinfra" {
  source = "./baseinfra"
  cluster = var.cluster
}

//module "mysql" {
//  source = "./mysql"
//  cluster_id = module.baseinfra.cluster_id
//  cluster_nodes = module.baseinfra.cluster_nodes
//  log_project_name = module.baseinfra.log_project_name
//  vpc_id = module.baseinfra.vpc_id
//  vswitch_ids = module.baseinfra.vswitch_ids
//  tiller_sa = module.baseinfra.tiller_sa
////  baseinfra = module.baseinfra.base_infra_details
//  cluster = var.cluster
////  helm-details = module.baseinfra.helm-details
////  k8s-details = module.baseinfra.k8s-details
////  vpc-details = module.baseinfra.vpc-details
//}
////
//module "oss" {
//  source = "./oss"
//  cluster_id = module.baseinfra.cluster_id
//  cluster_nodes = module.baseinfra.cluster_nodes
//  log_project_name = module.baseinfra.log_project_name
//  vpc_id = module.baseinfra.vpc_id
//  vswitch_ids = module.baseinfra.vswitch_ids
//  tiller_sa = module.baseinfra.tiller_sa
//  cluster = var.cluster
//}