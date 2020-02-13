locals {
  instances = {
    "bucket1" : {
      "acl" : "private"
    }
    "bucket2" : {
      "acl" : "public-read"
    }
  }
}

resource "alicloud_oss_bucket" "bucket" {
  for_each = local.instances
  bucket = "${var.cluster.name}-${each.key}"
  acl    = each.value["acl"]

  tags = {
    Name = "${var.cluster.name}-${each.key}"
  }
}

resource "alicloud_ram_policy" "readonly" {
  for_each = local.instances
  name     = "${var.cluster.name}-${each.key}-readonly"
  description = "${var.cluster.name}-${each.key}-readonly"
  document = <<EOF
  {
    "Statement": [
      {
        "Action": [
          "oss:Get*",
          "oss:List*",
          "oss:GetObject",
          "oss:GetObjectAcl",
          "oss:ListParts",
          "oss:ListObjects",
          "oss:ListBuckets",
          "oss:GetBucketLocation",
          "oss:ListMultipartUploads",
          "oss:GetBucketAcl",
          "oss:GetBucketReferer",
          "oss:GetBucketLogging",
          "oss:GetBucketWebsite",
          "oss:GetBucketLifecycle",
          "oss:GetBucketCors",
          "oss:GetBucketReplication",
          "oss:GetBucketReplicationLocation",
          "oss:GetBucketReplicationProgress"
        ],
        "Effect": "Allow",
        "Resource": [
          "arn:aws:s3:::${alicloud_oss_bucket.bucket[each.key].bucket}",
          "arn:aws:s3:::${alicloud_oss_bucket.bucket[each.key].bucket}/*"
        ]
      }
    ],
      "Version": "1"
  }
  EOF
}

resource "alicloud_ram_policy" "readwrite" {
  for_each = local.instances
  name        = "${var.cluster.name}-${each.key}-readwrite"
  description = "${var.cluster.name}-${each.key}-readwrite"
  document = <<EOF
  {
    "Version": "1",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": [
          "oss:*"
        ],
        "Resource": [
          "acs:oss:*:*:${alicloud_oss_bucket.bucket[each.key].bucket}",
          "acs:oss:*:*:${alicloud_oss_bucket.bucket[each.key].bucket}/*"
        ]
      }
    ]
  }
  EOF
}
