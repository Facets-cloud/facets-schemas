//output "infra_details" {
//  value = {
//    base_infra_details = {
//      k8s-details = module.baseinfra.base_infra_details.vpc_details
//      vpc-details = module.baseinfra.base_infra_details.k8s_details
//      helm-details = module.baseinfra.base_infra_details.helm_details
//    }
//    resources = {
////      mysql = module.mysql.mysql_details
////      oss = module.oss.oss_details
//    }
//  }
//}

//output "log-project-name" {
//  value = module.baseinfra.log-project-name
//}