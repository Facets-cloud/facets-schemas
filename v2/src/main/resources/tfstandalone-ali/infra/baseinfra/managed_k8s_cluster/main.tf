module "managed-k8s" {
  source = "terraform-alicloud-modules/managed-kubernetes/alicloud"
  profile = "Your-profile-name"

  k8s_name_prefix = "tf-managed-k8s"
  new_vpc = true
  vpc_cidr = var.cluster.vpcCIDR
  vswitch_cidrs = concat(
  var.cluster.privateSubnetCIDR
  )
  new_sls_project = true
  kube_config_path = "~/.captf/kube/ali/config"
}