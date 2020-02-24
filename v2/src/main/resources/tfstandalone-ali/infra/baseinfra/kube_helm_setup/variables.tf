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

variable "k8s_cluster_id" {
  type = string
}

variable "kube_config_file_path" {
  type = string
}

variable "vswitch_ids" {
  type = list(string)
}

variable "wait_for_cluster_cmd" {
  description = "Custom local-exec command to execute for determining if the alicloud k8s cluster is healthy. Cluster endpoint will be available as an environment variable called ENDPOINT"
  type        = string
  default     = "until curl -k -s $ENDPOINT/healthz >/dev/null; do sleep 4; done"
}

variable "security_group_id" {
  type = string
}