module "k8s-cluster" {
  source          = "terraform-aws-modules/eks/aws"
  cluster_name    = "${var.name}-k8s-cluster"
  cluster_version = "1.14"
  subnets         = "${var.subnets}"
  vpc_id          = "${var.vpc_id}"
  manage_aws_auth = false
  worker_groups = [
    {
      instance_type = "m4.large"
      asg_max_size  = 2
    }
  ]
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
  version                = "~> 1.10"
}

provider "helm" {
  kubernetes {
    config_path = module.k8s-cluster.kubeconfig_filename
  }
  version = "~> 0.10.4"
}

resource kubernetes_service_account "capillary-cloud-admin" {
  metadata {
    name = "capillary-cloud-admin"
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

data kubernetes_secret "capillary-cloud-admin-token" {
  metadata {
    name = kubernetes_service_account.capillary-cloud-admin.default_secret_name
  }
}

resource "restapi_object" "vpc_instance" {
  path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
  read_path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
  destroy_path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
  update_path = "/internal/capillarycloud/api/infraResources/k8sClusters/${var.infraResourceName}/instances/${var.name}"
  data = <<EOF
{
  "apiEndpoint": "${module.k8s-cluster.cluster_endpoint}",
  "token": "${data.kubernetes_secret.capillary-cloud-admin-token.data["token"]}"
}
EOF
}

