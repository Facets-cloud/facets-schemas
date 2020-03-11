output k8s_details {
  value = {
    auth = {
      host = data.aws_eks_cluster.cluster.endpoint
      cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
      token = data.aws_eks_cluster_auth.cluster.token
    }
    helm_details = {
      tiller_sa = kubernetes_service_account.tiller.metadata[0].name
    }
    node_group_iam_role_arn = try(module.k8s-cluster.node_groups["standard"].node_role_arn, "na")
  }
}