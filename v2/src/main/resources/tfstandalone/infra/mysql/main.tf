locals {
  stackName = var.cluster.stackName
  definitions = fileset("../stacks/${local.stackName}/mysql/instances", "*.json")
  instances = {
    for def in local.definitions:
        replace(def, ".json", "") => jsondecode(file("../stacks/${local.stackName}/mysql/instances/${def}"))
  }

  k8s_service_names_transpose = transpose({
    for i in values(local.instances):
    i["name"] => i["k8s_service_names"]
    })

  k8s_service_names_map = {
    for k, v in local.k8s_service_names_transpose:
        k => element(v, 0)
  }

  schema_files = {
    for o in flatten([
      for i, j in local.instances:
      [
        for f in fileset(j["schema_dir"], "*/*.sql"):
          {
            file_name = f
            instance_name = i
            key = "${i}_${f}"
            db = element(split("/", f), 0)
            table = element(split(".", element(split("/", f), 1)), 0)
          }
      ]
    ]):
      o.key => o
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
  name                 = replace("${var.cluster.name}${each.key}", "-", "")
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

resource "null_resource" "schema_sync" {
  for_each = local.schema_files
  provisioner "local-exec" {
    command = "/bin/bash scripts/sync_table.sh ${aws_db_instance.rds-instance[each.value["instance_name"]].address} root ${random_string.root_password[each.value["instance_name"]].result} ${each.value["db"]} ${each.value["table"]} ${each.value["file_name"]}"
  }
}