//output "helm_details" {
//  value = {
//    tiller_sa = kubernetes_service_account.tiller.metadata[0].name
//  }
//}

output "tiller_sa" {
  value = kubernetes_service_account.tiller.metadata[0].name
}