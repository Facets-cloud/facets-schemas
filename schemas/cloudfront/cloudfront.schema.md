## Introduction

A CloudFront intent which can be used to create AWS CloudFront resource in AWS Cloud

## Properties

| Property   | Type                | Required | Description                           |
|------------|---------------------|----------|---------------------------------------|
| `flavor`   | string              | **Yes**  | Possible values are: `default`.       |
| `kind`     | string              | **Yes**  | Possible values are: `cloudfront`.    |
| `spec`     | [object](#spec)     | **Yes**  |                                       |
| `version`  | string              | **Yes**  | Possible values are: `0.1`, `latest`. |
| `advanced` | [object](#advanced) | No       |                                       |
| `out`      | [object](#out)      | No       |                                       |

## advanced

### Properties

| Property     | Type                  | Required | Description                                                                                                                                                                                      |
|--------------|-----------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `cloudfront` | [object](#cloudfront) | No       | The advanced section of CloudFront module. You can pass the values as per terraform modules inputs - <https://registry.terraform.io/modules/terraform-aws-modules/cloudfront/aws/2.9.3?tab=inputs> |

### cloudfront

The advanced section of CloudFront module. You can pass the values as per terraform modules inputs - <https://registry.terraform.io/modules/terraform-aws-modules/cloudfront/aws/2.9.3?tab=inputs>

#### Properties

| Property                  | Type                               | Required | Description                                                                                                                                                                            |
|---------------------------|------------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `custom_error_responses`  | [object](#custom_error_responses)  | No       | One or more custom error response elements.                                                                                                                                            |
| `ordered_cache_behaviors` | [object](#ordered_cache_behaviors) | No       | An ordered map of cache behaviors resource for this distribution. Pass the map of object from top to bottom in order of precedence. The topmost cache behavior will have precedence 0. |

## out

### Properties

| Property     | Type                  | Required | Description                                 |
|--------------|-----------------------|----------|---------------------------------------------|
| `attributes` | [object](#attributes) | No       | The output attributes for CloudFront module |

### attributes

The output attributes for CloudFront module

#### Properties

| Property                                     | Type   | Required | Description                                           |
|----------------------------------------------|--------|----------|-------------------------------------------------------|
| `cloudfront_distribution_arn`                | string | No       | The ARN (Amazon Resource Name) for the distribution   |
| `cloudfront_distribution_domain_name`        | string | No       | The domain name corresponding to the distribution     |
| `cloudfront_distribution_etag`               | string | No       | The current version of the distribution's information |
| `cloudfront_distribution_id`                 | string | No       | The identifier for the distribution                   |
| `cloudfront_distribution_last_modified_time` | string | No       | The date and time the distribution was last modified  |

## spec

### Properties

| Property                 | Type                              | Required | Description                                                                                                                                                                                                                                                               |
|--------------------------|-----------------------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `aliases`                | [object](#aliases)                | **Yes**  | Extra CNAMEs (alternate domain names), if any, for this distribution. Follow this url <https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/CNAMEs.html#alternate-domain-names-requirements> to understand the requirements for using alternate domain names. |
| `default_cache_behavior` | [object](#default_cache_behavior) | **Yes**  | The default cache behavior for this distribution. Please check this url for supported attributes - <https://registry.terraform.io/providers/hashicorp/aws/3.74.0/docs/resources/cloudfront_distribution.html#default-cache-behavior-arguments>                              |
| `origins`                | [object](#origins)                | **Yes**  | One or more origins for this distribution (multiples allowed).                                                                                                                                                                                                            |
| `viewer_certificate`     | [object](#viewer_certificate)     | **Yes**  | The SSL configuration for this distribution. Please check this url for supported attributes - <https://registry.terraform.io/providers/hashicorp/aws/3.74.0/docs/resources/cloudfront_distribution.html#viewer-certificate-arguments>                                       |

### aliases

Extra CNAMEs (alternate domain names), if any, for this distribution. Follow this url <https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/CNAMEs.html#alternate-domain-names-requirements> to understand the requirements for using alternate domain names.

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `<alias>` | string | **Yes** | Extra CNAME (alternate domain name) |

### default_cache_behavior

The default cache behavior for this distribution. Please check this url for supported attributes - <https://registry.terraform.io/providers/hashicorp/aws/3.74.0/docs/resources/cloudfront_distribution.html#default-cache-behavior-arguments>

#### Properties

| Property                 | Type   | Required | Description                                                                                                                                                                                                                                            |
|--------------------------|--------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `allowed_methods`        | array  | **Yes**  | Controls which HTTP methods CloudFront processes and forwards to your Amazon S3 bucket or your custom origin.                                                                                                                                          |
| `cached_methods`         | array  | **Yes**  | Controls whether CloudFront caches the response to requests using the specified HTTP methods.                                                                                                                                                          |
| `target_origin_id`       | string | **Yes**  | The value of ID for the origin that you want CloudFront to route requests to when a request matches the path pattern for the default cache behavior. This will be key of the `spec.origins` attribute.                                                 |
| `viewer_protocol_policy` | string | **Yes**  | Use this element to specify the protocol that users can use to access the files in the origin specified by TargetOriginId when a request matches the path pattern in PathPattern. Possible values are: `allow-all`, `https-only`, `redirect-to-https`. |

### origins

One or more origins for this distribution (multiples allowed).

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `<origin_id>.domain_name` | string | **Yes** | The DNS domain name of either the S3 bucket, or web site of your custom origin. |

### viewer_certificate

The SSL configuration for this distribution. Please check this url for supported attributes - <https://registry.terraform.io/providers/hashicorp/aws/3.74.0/docs/resources/cloudfront_distribution.html#viewer-certificate-arguments>

#### Properties

| Property                         | Type    | Required | Description                                                                                                                                                                                                                                                                                                                                                                                                                           |
|----------------------------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `acm_certificate_arn`            | string  | No       | The ARN of the AWS Certificate Manager certificate that you wish to use with this distribution. Specify this, cloudfront_default_certificate, or iam_certificate_id. The ACM certificate must be in US-EAST-1                                                                                                                                                                                                                         |
| `cloudfront_default_certificate` | boolean | No       | If you want viewers to use HTTPS to request your objects and you're using the CloudFront domain name for your distribution.                                                                                                                                                                                                                                                                                                           |
| `iam_certificate_id`             | string  | No       | The IAM certificate identifier of the custom viewer certificate for this distribution if you are using a custom domain.                                                                                                                                                                                                                                                                                                               |
| `minimum_protocol_version`       | string  | No       | The minimum version of the SSL protocol that you want CloudFront to use for HTTPS connections. Can only be set if `cloudfront_default_certificate = false`. See all possible values in this <https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/secure-connections-supported-viewer-protocols-ciphers.html> table under `Security policy.` Some examples include: `TLSv1.2_2019` and `TLSv1.2_2021`. Default: `TLSv1`.  |
|                                  |         |          | NOTE: If you are using a custom certificate (specified with `acm_certificate_arn` or `iam_certificate_id`), and have specified `sni-only` in `ssl_support_method`, `TLSv1` or later must be specified. If you have specified `vip` in `ssl_support_method`, only `SSLv3` or `TLSv1` can be specified. If you have specified `cloudfront_default_certificate`, `TLSv1` must be specified.                                              |
| `ssl_support_method`             | string  | No       | Specifies how you want CloudFront to serve HTTPS requests. One of `vip` or `sni-only`. Required if you specify `acm_certificate_arn` or `iam_certificate_id`.                                                                                                                                                                                                                                                                         |
|                                  |         |          | NOTE: `vip` causes CloudFront to use a dedicated IP address and may incur extra charges. Possible values are: `vip`, `sni-only`.                                                                                                                                                                                                                                                                                                      |

### Flavors

* `default`
