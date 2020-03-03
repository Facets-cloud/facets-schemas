output "oss_details" {
  value = {
  for i in keys(local.instances):
  i => {
    bucket_name: alicloud_oss_bucket.bucket[i].bucket
    bucket_arn: alicloud_oss_bucket.bucket[i].id
    iam_policies: {
      READ_ONLY: alicloud_ram_policy.readonly[i]
      READ_WRITE: alicloud_ram_policy.readwrite[i]
    }
  }
  }
}