variable "cluster" {
  type = object({
    name = string
    aliRegion = string
    azs = list(string)
    privateSubnetCIDR = list(string)
    publicSubnetCIDR  = list(string)
    vpcCIDR = string
    stackName = string
  })
}

variable "kube_config_file_path" {
  type = string
}

variable "wait_for_cluster_cmd" {
  description = "Custom local-exec command to execute for determining if the alicloud k8s cluster is healthy. Cluster endpoint will be available as an environment variable called ENDPOINT"
  type        = string
  default     = "until curl -k -s $ENDPOINT/healthz >/dev/null; do sleep 4; done"
}

variable "vpc_details" {
  type = object({
    vpc_id = string
    vswitch_ids = list(string)
  })
}

variable "k8s_details" {
  type = object({
    cluster_id = string
    cluster_nodes = any
    security_group_id = string
    log_project_name = string
  })
}

variable "ec2_token_refresher_key_id" {
  type = string
}

variable "ec2_token_refresher_key_secret" {
  type = string
}