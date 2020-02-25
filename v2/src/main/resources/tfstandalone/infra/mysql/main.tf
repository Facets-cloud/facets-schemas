locals {
  instances = {
    "db1" = {
      name = "db1"
      k8s_service_names = ["mydb1", "yourdb1"]
    }
    "db2" = {
      name = "db2"
      k8s_service_names = ["mydb2", "yourdb2"]
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

resource "aws_security_group" "allow_mysql" {
  for_each = local.instances
  name = "allow_mysql_${var.cluster.name}-${each.key}"
  vpc_id = var.baseinfra.vpc_details.vpc_id
  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = [var.cluster.vpcCIDR, "172.31.0.0/16"]
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "random_string" "root_password" {
  for_each = local.instances
  length = 12
  override_special = "#$!"
}

resource "aws_db_subnet_group" "rds-subnet-group" {
  for_each = local.instances
  name       = "${var.cluster.name}-${each.key}-subnet-group"
  subnet_ids = var.baseinfra.vpc_details.private_subnets
}

resource "aws_db_instance" "rds-instance" {
  for_each = local.instances
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "mysql"
  engine_version       = "5.7"
  instance_class       = "db.t2.micro"
  name                 = "${var.cluster.name}${each.key}"
  username             = "root"
  password             = random_string.root_password[each.key].result
  parameter_group_name = "default.mysql5.7"
  db_subnet_group_name = aws_db_subnet_group.rds-subnet-group[each.key].name
  skip_final_snapshot  = true
  vpc_security_group_ids = [aws_security_group.allow_mysql[each.key].id]
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
    external_name = aws_db_instance.rds-instance[each.value].address
  }
}