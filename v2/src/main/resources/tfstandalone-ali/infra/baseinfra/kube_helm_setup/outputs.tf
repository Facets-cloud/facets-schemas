output "helm_details" {
  value = {
    tiller_sa = kubernetes_service_account.tiller.metadata[0].name
  }
}