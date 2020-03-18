locals {
  stackName = var.cluster.stackName
  definitions = fileset("../stacks/${local.stackName}/sqs/instances", "*.json")
  instances = {
  for def in local.definitions:
  replace(def, ".json", "") => jsondecode(file("../stacks/${local.stackName}/sqs/instances/${def}"))
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

resource "aws_sqs_queue" "sqs_queue"  {
  for_each = local.instances
  name                       = "sqs-${var.cluster.name}-${each.key}"
  delay_seconds              = 0
  max_message_size           = 262144
  message_retention_seconds  = 345600
  receive_wait_time_seconds  = 0
  visibility_timeout_seconds = 30
}

resource "aws_iam_policy" "consumer_policy" {
  for_each = aws_sqs_queue.sqs_queue
  name = "${each.value.name}_consumer"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "sqs:GetQueueAttributes",
        "sqs:GetQueueUrl",
        "sqs:ReceiveMessage",
        "sqs:DeleteMessage*",
        "sqs:PurgeQueue",
        "sqs:ChangeMessageVisibility*"
      ],
      "Resource": [
        "${each.value.arn}"
      ]
    }
  ]
}
EOF

}

resource "aws_iam_policy" "producer_policy" {
  for_each = aws_sqs_queue.sqs_queue
  name = "${each.value.name}_producer"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "sqs:GetQueueAttributes",
        "sqs:GetQueueUrl",
        "sqs:SendMessage*"
      ],
      "Resource": [
        "${each.value.arn}"
      ]
    }
  ]
}
EOF

}

