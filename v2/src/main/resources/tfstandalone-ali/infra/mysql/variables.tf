variable "cluster" {
  type = object({
    name = string
    aliRegion = string
    azs = list(string)
    privateSubnetCIDR = list(string)
    publicSubnetCIDR  = list(string)
    vpcCIDR = string
  })
}

//variable "helm-details" {
//  type = object({
//    tiller_sa = string
//  })
//}
//
//variable "k8s-details" {
//  type = object({
//    cluster_id = string
//    cluster_nodes = string
//    security_group_id = string
//  })
//}
//
//variable "vpc-details" {
//  type = object({
//    vpc_id = string
//    vswitch_ids = list(string)
//  })
//}

//variable "baseinfra" {
//  type = object({
//    vpc_details = object({
//      vpc_id = string
//      vswitch_ids = list(string)
//    })
//    k8s_details = object({
//      cluster_id = string
//      cluster_nodes = string
//      security_group_id = list(string)
//      log_project_name = string
//    })
//    helm_details = object({
//      tiller_sa = string
//    })
//  })
//}

variable "vswitch_ids" {
  type = list(string)
}

variable "vpc_id" {
  type = string
}

variable "log_project_name" {
  type = string
}

variable "cluster_nodes" {
  type = any
}

variable "cluster_id" {
  type = string
}

variable "tiller_sa" {
  type = string
}