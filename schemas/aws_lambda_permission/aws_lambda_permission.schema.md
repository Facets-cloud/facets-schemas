# AWS Lambda Permission Schema docs

## Properties

| Property   | Type                | Required | Description                                                                         |
|------------|---------------------|----------|-------------------------------------------------------------------------------------|
| `flavor`   | string              | **Yes**  | Possible values are: `default`.                                                     |
| `kind`     | string              | **Yes**  | Possible values are: `aws_lambda_permission`.                                       |
| `spec`     | [object](#spec)     | **Yes**  |                                                                                     |
| `version`  | string              | **Yes**  | Possible values are: `0.1`, `latest`.                                               |
| `advanced` | [object](#advanced) | No       |                                                                                     |
| `out`      | [object](#out)      | No       | The output for your aws lambda permission module, this can be generated or provided |

## advanced

### Properties

| Property                | Type                             | Required | Description                                                  |
|-------------------------|----------------------------------|----------|--------------------------------------------------------------|
| `aws_lambda_permission` | [object](#aws_lambda_permission) | No       | The advanced section of all the aws lambda permission module |

### aws_lambda_permission

The advanced section of all the aws lambda permission module

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## out

The output for your aws lambda permission module, this can be generated or provided

### Properties

| Property     | Type                  | Required | Description                                       |
|--------------|-----------------------|----------|---------------------------------------------------|
| `attributes` | [object](#attributes) | No       | The advanced section of all the aws lambda module |

### attributes

The advanced section of all the aws lambda module

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## spec

### Properties

| Property           | Type                        | Required | Description                                         |
|--------------------|-----------------------------|----------|-----------------------------------------------------|
| `allowed_triggers` | [object](#allowed_triggers) | **Yes**  | Map of allowed triggers to create Lambda permission |
| `lambda_name`      | string                      | **Yes**  | The name of the lambda                              |
| `lambda_version`   | string                      | **Yes**  | The version of the lambda                           |

### allowed_triggers

Map of allowed triggers to create Lambda permission

#### Properties

| Property   | Type                | Required | Description                                                                                                                  |
|------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------|
| `^[a-zA-Z0-9_.-]*$` | [object](#`^[a-zA-Z0-9_.-]*$`) | No       | This is the name of the output interface this can be any name depending on the number of prefix domains in the domains block |

#### `^[a-zA-Z0-9_.-]*$` 

This is the name of the output interface this can be any name depending on the number of prefix domains in the domains block

##### Properties

| Property             | Type   | Required | Description                                                                                                                                                                                                                                                                                                                                                                                                                              |
|----------------------|--------|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `action`             | string | No       | The AWS Lambda action you want to allow in this statement.                                                                                                                                                                                                                                                                                                                                                                               |
| `event_source_token` | string | No       | The Event Source Token to validate. Used with Alexa Skills.                                                                                                                                                                                                                                                                                                                                                                              |
| `principal`          | string | No       | The principal who is getting this permission e.g., s3.amazonaws.com, an AWS account ID, or AWS IAM principal, or AWS service principal such as events.amazonaws.com or sns.amazonaws.com                                                                                                                                                                                                                                                 |
| `source_account`     | string | No       | his parameter is used when allowing cross-account access, or for S3 and SES. The AWS account ID (without a hyphen) of the source owner.                                                                                                                                                                                                                                                                                                  |
| `source_arn`         | string | No       | When the principal is an AWS service, the ARN of the specific resource within that service to grant permission to. Without this, any resource from principal will be granted permission – even if that resource is from another account. For S3, this should be the ARN of the S3 Bucket. For EventBridge events, this should be the ARN of the EventBridge Rule. For API Gateway, this should be the ARN of the API, as described here. |
| `statement_id`       | string | No       | A unique statement identifier. By default generated by Terraform.                                                                                                                                                                                                                                                                                                                                                                        |

