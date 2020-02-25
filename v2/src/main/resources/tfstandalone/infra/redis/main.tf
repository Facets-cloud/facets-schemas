locals {
  instances = {
    "redis1" = {
      name = "redis1"
      k8s_service_names = ["myredis1", "yourredis1"]
      parameter_overrides = {
        maxmemory-policy = "allkeys-lru"
      }
    }
  }

  k8s_service_names_transpose = transpose({
    for i in values(local.instances):
      i["name"] => i["k8s_service_names"]
  })

  k8s_service_names_map = {
    for k, v in local.k8s_service_names_transpose:
      k => element(v, 0)
  }

}

resource "aws_elasticache_parameter_group" "param-group" {
  for_each = local.instances
  name   = "cache-params-${var.cluster.name}-${each.key}"
  family = "redis3.2"

  dynamic "parameter" {
    for_each = each.value["parameter_overrides"]
    content {
      name = parameter.key
      value = parameter.value
    }
  }
}

resource "aws_elasticache_subnet_group" "cache-subnet-group" {
  for_each = local.instances
  name       = "${var.cluster.name}-${each.key}-subnet-group"
  subnet_ids = var.baseinfra.vpc_details.private_subnets
}

resource "aws_security_group" "allow_redis" {
  for_each = local.instances
  name = "allow_redis_${var.cluster.name}-${each.key}"
  vpc_id = var.baseinfra.vpc_details.vpc_id
  ingress {
    from_port   = 6379
    to_port     = 6379
    protocol    = "tcp"
    cidr_blocks = [var.cluster.vpcCIDR, "172.31.0.0/16"]
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
  }
}

resource "aws_elasticache_replication_group" "cache-cluster" {
  for_each = local.instances
  replication_group_id = "${var.cluster.name}-${each.key}"
  replication_group_description = "${var.cluster.name}-${each.key}"
  engine               = "redis"
  node_type            = "cache.m4.large"
  number_cache_clusters = 2
  parameter_group_name = aws_elasticache_parameter_group.param-group[each.key].name
  engine_version       = "3.2.10"
  port                 = 6379
  subnet_group_name = aws_elasticache_subnet_group.cache-subnet-group[each.key].name
  security_group_ids = [aws_security_group.allow_redis[each.key].id]
  automatic_failover_enabled = true
}

provider "kubernetes" {
  host                   = var.baseinfra.k8s_details.auth.host
  cluster_ca_certificate = var.baseinfra.k8s_details.auth.cluster_ca_certificate
  token                  = var.baseinfra.k8s_details.auth.token
  load_config_file       = false
  version                = "~> 1.10"
}

resource "kubernetes_service" "mysql-k8s-service" {
  for_each = local.k8s_service_names_map
  metadata {
    name = each.key
  }
  spec {
    type = "ExternalName"
    external_name = aws_elasticache_replication_group.cache-cluster[each.value].primary_endpoint_address
  }
}