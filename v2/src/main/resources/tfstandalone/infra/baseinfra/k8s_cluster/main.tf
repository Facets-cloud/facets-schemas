module "k8s-cluster" {
  source          = "terraform-aws-modules/eks/aws"
  cluster_name    = "${var.cluster.name}-k8s-cluster"
  cluster_version = "1.14"
  subnets         = "${var.vpc_details.private_subnets}"
  vpc_id          = "${var.vpc_details.vpc_id}"
  manage_aws_auth = false
  node_groups = {
    standard = {
      desired_capacity = 1
      max_capacity     = 10
      min_capacity     = 1
      instance_type = "t3.large"
    }
  }

  node_groups_defaults = {
    ami_type  = "AL2_x86_64"
    disk_size = 50
  }

  version = "8.1.0"
  write_kubeconfig = false
}

data "aws_eks_cluster" "cluster" {
  name = module.k8s-cluster.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.k8s-cluster.cluster_id
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  token                  = data.aws_eks_cluster_auth.cluster.token
  load_config_file       = false
  version                = "~>1.10.0"
}

provider "helm" {
  kubernetes {
    host                   = data.aws_eks_cluster.cluster.endpoint
    cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
    token                  = data.aws_eks_cluster_auth.cluster.token
    load_config_file       = false
  }
  version = "~> 0.10.4"
  service_account = kubernetes_service_account.tiller.metadata[0].name
  install_tiller = true
}

resource kubernetes_service_account "capillary-cloud-admin" {
  metadata {
    name = "capillary-cloud-admin"
  }
}

resource kubernetes_service_account "tiller" {
  metadata {
    name = "tiller"
    namespace = "kube-system"
  }
}

resource kubernetes_cluster_role_binding "capillary-cloud-admin-crb" {
  metadata {
    name = "capillary-cloud-admin-crb"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "cluster-admin"
  }

  subject {
    kind      = "ServiceAccount"
    name      = kubernetes_service_account.capillary-cloud-admin.metadata[0].name
    namespace = "default"
  }
}

resource kubernetes_cluster_role_binding "tiller-crb" {
  metadata {
    name = "tiller-crb"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind      = "ClusterRole"
    name      = "cluster-admin"
  }

  subject {
    kind      = "ServiceAccount"
    name      = kubernetes_service_account.tiller.metadata[0].name
    namespace = "kube-system"
  }
}

data kubernetes_secret "capillary-cloud-admin-token" {
  metadata {
    name = kubernetes_service_account.capillary-cloud-admin.default_secret_name
  }
}

data "helm_repository" "stable" {
  name = "stable"
  url  = "https://kubernetes-charts.storage.googleapis.com"
}

resource "helm_release" "cluster-autoscaler" {
  depends_on = [module.k8s-cluster]
  name       = "cluster-autoscaler"
  repository = data.helm_repository.stable.metadata[0].name
  chart      = "cluster-autoscaler"
  version    = "6.2.0"

  set_string {
    name  = "autoDiscovery.clusterName"
    value = "${var.cluster.name}-k8s-cluster"
  }

  set_string {
    name  = "awsRegion"
    value = var.cluster.awsRegion
  }

  set {
    name = "rbac.create"
    value = "true"
  }
}

resource "aws_iam_policy" "kube2iam-nodegroup-policy" {
  name        = "${var.cluster.name}-kube2iam-nodegroup-policy"
  path        = "/"
  description = "${var.cluster.name}-kube2iam-nodegroup-policy"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "sts:AssumeRole"
      ],
      "Effect": "Allow",
      "Resource": "*"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "kube2iam-attach" {
  role       = module.k8s-cluster.worker_iam_role_name
  policy_arn = aws_iam_policy.kube2iam-nodegroup-policy.arn
}

resource "helm_release" "kube2iam" {
  depends_on = [module.k8s-cluster]
  name       = "kube2iam"
  repository = data.helm_repository.stable.metadata[0].name
  chart      = "kube2iam"
  version    = "2.1.0"

  set {
    name = "rbac.create"
    value = "true"
  }

  set {
    name = "host.iptables"
    value = "true"
  }

  set {
    name = "verbose"
    value = "true"
  }

  set_string {
    name = "host.interface"
    value = "eni+"
  }

  set {
    name = "extraArgs.auto-discover-default-role"
    value = "true"
  }
}

resource "helm_release" "cluster-overprovisioner" {
  name       = "cluster-overprovisioner"
  repository = data.helm_repository.stable.metadata[0].name
  chart      = "cluster-overprovisioner"
  version    = "0.2.6"

  values = [
<<DEPLOYMENTS
deployments:
  - name: overprovisioner
    annotations: {}
    replicaCount: 1
    nodeSelector: {}
    resources:
      requests:
        cpu: 1500m
        memory: 1500Gi
    tolerations: []
    affinity: {}
    labels: {}
DEPLOYMENTS
  ]
}
//resource "restapi_object" "vpc_instance" {
//  path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
//  read_path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
//  destroy_path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
//  update_path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
//  data = <<EOF
//{
//  "apiEndpoint": "${module.k8s-cluster.cluster_endpoint}",
//  "token": "${data.kubernetes_secret.capillary-cloud-admin-token.data["token"]}"
//}
//EOF
//}
