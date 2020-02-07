output "s3_details" {
  value = {
    for i in keys(local.instances):
    i => {
      bucket_name: aws_s3_bucket.bucket[i].bucket
      bucket_arn: aws_s3_bucket.bucket[i].arn
      iam_policies: {
        READ_ONLY: aws_iam_policy.readonly[i].arn
        READ_WRITE: aws_iam_policy.readwrite[i].arn
      }
    }
  }
}