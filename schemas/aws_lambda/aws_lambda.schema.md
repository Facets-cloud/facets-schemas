# AWS Lambda Configuration JSON Schema

This JSON Schema defines the configuration for AWS Lambda resources.

## Schema Overview

| Key               | Type   | Description                                           | Required | Example                                |
|-------------------|--------|-------------------------------------------------------|----------|----------------------------------------|
| kind              | string | Specifies the kind of resource (e.g., "aws_lambda")   | Yes      | "aws_lambda"                           |
| flavor            | string | Specifies the flavor of the resource (e.g., "default")| Yes      | "default"                              |
| version           | string | Specifies the version of the resource                 | Yes      | "0.1", "latest"                        |
| metadata          | object | Additional metadata for the resource                  | Yes      | {"name": "lambda-s3"}                  |
| spec              | object | The specification for the AWS Lambda function         | Yes      | See `spec` properties below            |
| advanced          | object | Advanced configuration options for AWS Lambda         | No       | See `advanced` properties below        |
| provided          | boolean | Indicates if the resource is provided                | No       | false                                  |
| disabled          | boolean | Indicates if the resource is disabled                | No       | false                                  |
| out               | object | The output for the AWS Lambda module                  | No       | See `out` properties below             |

## `spec` Properties

| Key          | Type   | Description                                                                               | Required     | Example                                |
|--------------|--------|-------------------------------------------------------------------------------------------|--------------|----------------------------------------|
| description  | string | Description of your Lambda Function                                                       | Yes          | "This is a test lambda"                |
| handler      | string | Lambda Function entrypoint in your code                                                   | Yes          | "main.lambda_handler"                  |
| runtime      | string | Lambda Function runtime                                                                   | Yes          | "python3.8"                            |
| release      | object | The release object containing the artifacts , either S3 or container image                | yes          | see below                              |
| env          | object | A map that defines environment variables for the Lambda Function                          | No           | {}                                     |

## `release` Properties

| Key                                    | Type    | Description                                             | Required                                                         | Example                             |
|----------------------------------------|---------|---------------------------------------------------------|------------------------------------------------------------------|-------------------------------------|
| s3_path                                | string  | The full path of the zip file present in S3             | Conditional required if `image` and `build` are not present       | `lambda-vishnu-testing/lambda.zip` |
| image                                  | string  | The docker image of the application that you want to run| Conditional required if `s3_path` and `build` are not present     | docker image                       |
| build                                  | object  | The full path of the zip file present in S3             | Conditional required if `image` and `s3_path` are not present     | {}                                 |

## `build` Properties

| Key                                    | Type    | Description                                             | Required | Example        |
|----------------------------------------|---------|---------------------------------------------------------|----------|----------------|
| artifactory                            | string  | Parent artifactory name                                 | No       | ""             |
| name                                   | string  | Name of the artifactory                                 | No       | ""             |

## `advanced` Properties

| Key                                    | Type    | Description                                             | Required | Example        |
|----------------------------------------|---------|---------------------------------------------------------|----------|----------------|
| aws_lambda                             | object  | The advanced section of all the aws lambda module      | No       | See below       |

### `aws_lambda` Properties

| Key                                    | Type    | Description                                             | Required | Example        |
|----------------------------------------|---------|---------------------------------------------------------|----------|----------------|
| allowed_triggers                       | object  | Map of allowed triggers to create Lambda permissions   | No       | See below      |
| create_role                            | boolean | Controls whether IAM role for Lambda Function should be created | No       | true           |
| lambda_at_edge                        | boolean | Set this to true if using Lambda@Edge, to enable publishing, limit the timeout, and allow edgelambda.amazonaws.com to invoke the function | No | true |
| memory_size                           | integer | Amount of memory in MB your Lambda Function can use at runtime. Valid value between 128 MB to 10,240 MB (10 GB), in 64 MB increments | No | 1024 |
| publish                               | boolean | Map of allowed triggers to create Lambda permissions    | No       | true           |
| reserved_concurrent_executions         | integer | The amount of reserved concurrent executions for this Lambda Function. A value of 0 disables Lambda Function from being triggered and -1 removes any concurrency limitations. Defaults to Unreserved Concurrency Limits -1. | No | 10 |
| timeout                               | integer | The amount of time your Lambda Function has to run in seconds. | No | 30 |
| dead_letter_target_arn                 | string  | The ARN of an SNS topic or SQS queue to notify when an invocation fails. | No | "arn:aws:sns:us-west-2:123456789012:my-topic" |
| tracing_mode                          | string  | Tracing mode of the Lambda Function. Valid value can be either "PassThrough" or "Active." | No | "Active" |
| compatible_runtimes                   | array   | A list of Runtimes this layer is compatible with. Up to 5 runtimes can be specified. | No | ["nodejs12.x", "nodejs14.x"] |
| compatible_architectures              | array   | A list of Architectures Lambda layer is compatible with. Currently "x86_64" and "arm64" can be specified. | No | ["x86_64", "arm64"] |
| create_async_event_config             | boolean | Controls whether async event configuration for Lambda Function/Alias should be created | No | true |
| create_current_version_async_event_config | boolean | Whether to allow async event configuration on current version of Lambda Function (this will revoke permissions from previous version because Terraform manages only current resources) | No | false |
| create_unqualified_alias_async_event_config | boolean | Whether to allow async event configuration on unqualified alias pointing to $LATEST version | No | true |
| maximum_event_age_in_seconds          | integer | Maximum age of a request that Lambda sends to a function for processing in seconds. Valid values between 60 and 21600. | No | 3600 |
| maximum_retry_attempts                | integer | Maximum number of times to retry when the function returns an error. Valid values between 0 and 2. Defaults to 2. | No | 1 |
| destination_on_failure                | string  | Amazon Resource Name (ARN) of the destination resource for failed asynchronous invocations | No | "arn:aws:sqs:us-west-2:123456789012:my-queue" |
| destination_on_success                | string  | Amazon Resource Name (ARN) of the destination resource for successful asynchronous invocations | No | "arn:aws:sns:us-west-2:123456789012:my-topic" |
| event_source_mapping                  | object  | Map of event source mapping | No | {"batch_size": 10} |
| use_existing_cloudwatch_log_group     | boolean | Whether to use an existing CloudWatch log group or create new | No | true |
| cloudwatch_logs_retention_in_days     | integer | Specifies the number of days you want to retain log events in the specified log group. Possible values are: 1, 3, 5, 7, 14, 30, 60, 90, 120, 150, 180, 365, 400, 545, 731, 1827, and 3653. | No | 30 |
| provisioned_concurrent_executions     | integer | Amount of capacity to allocate. Set to 1 or greater to enable, or set to 0 to disable provisioned concurrency. | No | 5 |
| cloudwatch_logs_kms_key_id            | string  | The ARN of the KMS Key to use when encrypting log data. | No | "arn:aws:kms:us-west-2:123456789012:key/my-key" |
| image_config_entry_point              | array   | The ENTRYPOINT for the docker image | No | ["node", "index.js"] |
| image_config_command                  | array   | The CMD for the docker image | No | ["npm", "start"] |
| image_config_working_directory        | string  | The working directory for the docker image | No | "/app" |

## `out` Properties

| Key                                    | Type    | Description                                             | Required | Example        |
|----------------------------------------|---------|---------------------------------------------------------|----------|----------------|
| attributes                             | object  | The advanced section of all the aws lambda module      | No       | {}             |

## `attributes` Properties

| Key                                    | Type    | Description                                                | Required | Example        |
|----------------------------------------|---------|------------------------------------------------------------|----------|----------------|
| function_name                          | string  | The name of the Lambda Function                            | No       | ""             |
| function_arn                           | string  | The ARN of the Lambda Function                             | No       | ""             |
| function_invoke_arn                    | string  | The Invoke ARN of the Lambda Function                      | No       | ""             |
| function_qualified_arn                 | string  | The ARN identifying your Lambda Function Version           | No       | ""             |
| function_role_arn                      | string  | The ARN of the IAM role created for the Lambda Function    | No       | ""             |
| function_role_name                     | string  | The name of the IAM role created for the Lambda Function   | No       | ""             |
