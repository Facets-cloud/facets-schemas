provider "kubernetes" {
  version = "~>1.10.0"
  config_path = var.kube_config_file_path
//  load_config_file = true
}

provider "helm" {
  kubernetes {
    config_path = var.kube_config_file_path
//    load_config_file = true
  }
  version = "~> 0.10.4"
  service_account = kubernetes_service_account.tiller.metadata[0].name
  install_tiller = true
}

resource "null_resource" "wait_kube_config" {
  provisioner "local-exec" {
    command = "python infra/baseinfra/kube_helm_setup/wait_kube_config.py"
  }
}

resource kubernetes_service_account "capillary-cloud-admin" {
  depends_on = [null_resource.wait_kube_config]
  metadata {
    name = "capillary-cloud-admin"
  }
}

resource kubernetes_service_account "tiller" {
  depends_on = [null_resource.wait_kube_config]
  metadata {
    name = "tiller"
    namespace = "kube-system"
  }
}

resource kubernetes_cluster_role_binding "capillary-cloud-admin-crb" {
  depends_on = [kubernetes_service_account.capillary-cloud-admin]
  metadata {
    name = "capillary-cloud-admin-crb"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind = "ClusterRole"
    name = "cluster-admin"
  }

  subject {
    kind = "ServiceAccount"
    name = kubernetes_service_account.capillary-cloud-admin.metadata[0].name
    namespace = "default"
  }
}

resource kubernetes_cluster_role_binding "tiller-crb" {
  depends_on = [kubernetes_service_account.tiller]
  metadata {
    name = "tiller-crb"
  }

  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind = "ClusterRole"
    name = "cluster-admin"
  }

  subject {
    kind = "ServiceAccount"
    name = kubernetes_service_account.tiller.metadata[0].name
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
  url = "https://kubernetes-charts.storage.googleapis.com"
}

resource "helm_release" "cluster-autoscaler" {
  depends_on = [kubernetes_cluster_role_binding.capillary-cloud-admin-crb, kubernetes_cluster_role_binding.tiller-crb]
  name = "cluster-autoscaler"
  repository = data.helm_repository.stable.metadata[0].name
  chart = "cluster-autoscaler"
  version = "6.2.0"

  set_string {
    name = "autoDiscovery.clusterName"
    value = "${var.cluster.name}-k8s-cluster"
  }

  set_string {
    name = "aliRegion"
    value = var.cluster.aliRegion
  }

  set {
    name = "rbac.create"
    value = "true"
  }
}

resource "alicloud_ram_policy" "policy" {
  name = "${var.cluster.name}-kube2ram-nodegroup-policy"
  description = "${var.cluster.name}-kube2ram-nodegroup-policy"
  document = <<EOF
  {
    "Statement": [
      {
        "Action": [
          "sts:AssumeRole"
        ],
        "Effect": "Allow",
        "Resource": "*"
      }
    ],
      "Version": "1"
  }
  EOF
}

//resource "alicloud_ram_role_policy_attachment" "attach" {
//  policy_name = "${alicloud_ram_policy.policy.name}"
//  policy_type = "${alicloud_ram_policy.policy.type}"
//  role_name   = "${alicloud_ram_role.role.name}"
//}

//resource "helm_release" "kube2iam" {
//  name = "kube2iam"
//  repository = data.helm_repository.stable.metadata[0].name
//  chart = "kube2iam"
//  version = "2.1.0"
//
//  set {
//    name = "rbac.create"
//    value = "true"
//  }
//
//  set {
//    name = "host.iptables"
//    value = "true"
//  }
//
//  set {
//    name = "verbose"
//    value = "true"
//  }
//
//  set_string {
//    name = "host.interface"
//    value = "eni+"
//  }
//}
