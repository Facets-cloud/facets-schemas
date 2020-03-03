locals {
  k8s_cluster_id = var.k8s_details.cluster_id
}

provider "kubernetes" {
  version = "~>1.10.0"
  config_path = var.kube_config_file_path
}

provider "alicloud" {
  region     = var.cluster.aliRegion
  version = "1.71.1"
}

data "alicloud_cs_managed_kubernetes_clusters" "k8s_clusters" {
  ids = [local.k8s_cluster_id]
}

resource "null_resource" "wait_for_k8s_cluster" {
  provisioner "local-exec" {
    command = var.wait_for_cluster_cmd
    environment = {
      ENDPOINT = data.alicloud_cs_managed_kubernetes_clusters.k8s_clusters.clusters[0].connections.api_server_internet
    }
  }
}

provider "helm" {
  kubernetes {
    config_path = var.kube_config_file_path
  }
  version = "~> 0.10.4"
  service_account = kubernetes_service_account.tiller.metadata[0].name
  install_tiller = true
}

resource "alicloud_ess_scaling_group" "default" {
  min_size           = 1
  max_size           = 2
  scaling_group_name = "${var.cluster.name}-scaling-group"
  default_cooldown   = 20
  vswitch_ids        = var.vpc_details.vswitch_ids
  removal_policies   = ["OldestInstance", "NewestInstance"]
}

data "alicloud_instance_types" "default" {
  availability_zone = var.cluster.azs[0]
  cpu_core_count    = 2
  memory_size       = 4
}

data "alicloud_images" "default" {
  name_regex  = "^ubuntu_18.*64"
  most_recent = true
  owners      = "system"
}

resource "alicloud_ess_scaling_configuration" "default" {
  scaling_group_id  = alicloud_ess_scaling_group.default.id
  image_id          = data.alicloud_images.default.images[0].id
  instance_type     = data.alicloud_instance_types.default.instance_types[0].id
  security_group_id = var.k8s_details.security_group_id
  force_delete = true
  active = true
  enable = true
}

resource "alicloud_cs_kubernetes_autoscaler" "default" {
  cluster_id              = local.k8s_cluster_id

  nodepools {
    id                = alicloud_ess_scaling_group.default.id
    taints            = "c=d:NoSchedule"
    labels            = "a=b"
  }
  utilization             = "0.5"
  cool_down_duration      = "1m"
  defer_scale_in_duration = "1m"
}

resource kubernetes_service_account "capillary-cloud-admin" {
  depends_on = [null_resource.wait_for_k8s_cluster]
  metadata {
    name = "capillary-cloud-admin"
  }
}

resource kubernetes_service_account "tiller" {
  depends_on = [null_resource.wait_for_k8s_cluster, kubernetes_cluster_role_binding.tiller-crb]
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
    name = "tiller"
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
