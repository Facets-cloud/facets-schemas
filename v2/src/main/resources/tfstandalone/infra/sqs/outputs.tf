output "sqs_details" {
  value = {
  for i in keys(local.instances):
  i => {
    sqs_arn: aws_sqs_queue.sqs_queue[i].arn
    sqs_id: aws_sqs_queue.sqs_queue[i].id
  }
  }
}

output "sqs_consumer_policy" {
  value       = aws_iam_policy.consumer_policy.*.arn
}

output "sqs_producer_policy" {
  value       = aws_iam_policy.producer_policy.*.arn
}

