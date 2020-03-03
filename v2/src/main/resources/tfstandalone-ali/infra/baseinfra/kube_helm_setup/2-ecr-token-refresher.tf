resource "kubernetes_secret" "example" {
  metadata {
    name = "aws-ecr-token-refresher-secrets-nurego"
  }

  data = {
    aws_access_key_id = var.ec2_token_refresher_key_id
    aws_access_secret_key = var.ec2_token_refresher_key_secret
    aws_account = "486456986266"
    aws_region = "us-west-1"
  }
}

resource "kubernetes_cron_job" "demo" {
  metadata {
    name = "ecr-token-refresher-nurego"
  }
  spec {
    concurrency_policy            = "Allow"
    failed_jobs_history_limit     = 1
    schedule                      = "*/5 * * * *"
    starting_deadline_seconds     = 20
    successful_jobs_history_limit = 1
    suspend                       = false
    job_template {
      metadata {}
      spec {
        backoff_limit = 4
        template {
          metadata {}
          spec {
            service_account_name = "ecr-token-refresher-nurego"
            automount_service_account_token = true
            container {
              name    = "kubectl"
              image   = "xynova/aws-kubectl"
              image_pull_policy = "Always"
              command = ["/bin/sh", "-c", file("${path.module}/ecr-token-refresher-command")]
              env {
                name = "AWS_ACCOUNT"
                value_from {
                  secret_key_ref {
                    key = "aws_account"
                    name = "aws-ecr-token-refresher-secrets-nurego"
                  }
                }
              }
              env {
                name = "AWS_ACCESS_KEY_ID"
                value_from {
                  secret_key_ref {
                    key = "aws_access_key_id"
                    name = "aws-ecr-token-refresher-secrets-nurego"
                  }
                }
              }
              env {
                name = "AWS_SECRET_ACCESS_KEY"
                value_from {
                  secret_key_ref {
                    key = "aws_access_secret_key"
                    name = "aws-ecr-token-refresher-secrets-nurego"
                  }
                }
              }
              env {
                name = "AWS_REGION"
                value_from {
                  secret_key_ref {
                    key = "aws_region"
                    name = "aws-ecr-token-refresher-secrets-nurego"
                  }
                }
              }
            }
            restart_policy = "Never"
          }
        }
      }
    }
  }
}

resource "kubernetes_role" "ecr-token-refresher-role" {
  metadata {
    name = "ecr-token-refresher-nurego"
  }
  rule {
    api_groups = [""]
    resources = ["secrets"]
    verbs = ["get", "create", "delete"]
  }
  rule {
    api_groups = [""]
    resources = ["serviceaccounts"]
    verbs = ["get", "patch"]
  }
}

resource "kubernetes_service_account" "ecr-token-refresher-sa" {
  metadata {
    name = "ecr-token-refresher-nurego"
  }
}

resource "kubernetes_role_binding" "ecr-token-refresher-crb" {
  metadata {
    name = "name: ecr-token-refresher-nurego"
  }
  role_ref {
    api_group = "rbac.authorization.k8s.io"
    kind = "Role"
    name = "ecr-token-refresher-nurego"
  }
  subject {
    kind = "ServiceAccount"
    name = "ecr-token-refresher-nurego"
  }
}