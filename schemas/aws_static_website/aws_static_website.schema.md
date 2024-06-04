# AWS Static Website Schema docs

This module creates a destination ss3 bucket and uploads the extracted files from source bucket to destination bucket. cloudfront can be enabled for this s3 bucket

## Properties

| Property   | Type                | Required | Description                                                                         |
| ---------- | ------------------- | -------- | ----------------------------------------------------------------------------------- |
| `flavor`   | string              | **Yes**  | Possible values are: `default`.                                                     |
| `kind`     | string              | **Yes**  | Possible values are: `aws_static_website`.                                          |
| `spec`     | [object](#spec)     | **Yes**  |                                                                                     |
| `version`  | string              | **Yes**  | Possible values are: `0.1`, `latest`.                                               |
| `advanced` | [object](#advanced) | No       |                                                                                     |
| `out`      | [object](#out)      | No       | The output for your aws lambda permission module, this can be generated or provided |

## spec

### Properties

| Property  | Type               | Required | Description     |
| --------- | ------------------ | -------- | --------------- |
| `website` | [object](#website) | Yes      | Website details |
| `domains` | [object](#domains) | Yes      | Domains details |

## website

### Properties

| Property                       | Type   | Required | Description                                                                                                   |
| ------------------------------ | ------ | -------- | ------------------------------------------------------------------------------------------------------------- |
| `source_code_s3_path`          | string | Yes      | Path of the file to be extracted and uploaded to bucket                                                       |
| `cloudfront_enabled`           | string | No       | Flag to enable the cloudfront                                                                                 |
| `acm_certificate_arn`          | string | No       | AWS Arn of the acm cerificate to be used by cloudfront. The certificate should cover all the provided domains |

## domains

### Properties

| Property | Type   | Required | Description              |
| -------- | ------ | -------- | ------------------------ |
| `domain` | string | Yes      | Domain                   |
| `alias`  | string | No       | Alias name of the domain |

## advanced

### Properties

| Property     | Type                                                                                              | Required | Description                                             |
| ------------ | ------------------------------------------------------------------------------------------------- | -------- | ------------------------------------------------------- |
| `website`    | [object](#website_advanced)                                                                       | No       | The advanced section of website                         |
| `s3`         | [object](https://facets-cloud.github.io/facets-schemas/schemas/s3/s3.schema.md)                   | No       | The advanced section supported by aws s3 module         |
| `cloudfront` | [object](https://facets-cloud.github.io/facets-schemas/schemas/cloudfront/cloudfront.schema.json) | No       | The advanced section supported by aws cloudfront module |

## website_advanced

### Properties

| Property         | Type   | Required | Description                |
| ---------------- | ------ | -------- | -------------------------- |
| `index_document` | string | Yes      | Name of the index document |
| `error_document` | string | No       | Name of the error document |
