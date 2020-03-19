output "sqs_details" {
  value = {
  for i in keys(local.instances):
  i => {
    sqs_arn: aws_sqs_queue.sqs_queue[i].arn
    sqs_id: aws_sqs_queue.sqs_queue[i].id
    sqs_consumer_policy = aws_iam_policy.consumer_policy.*.arn
    sqs_producer_policy = aws_iam_policy.producer_policy.*.arn
  }
  }
}

