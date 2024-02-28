# AWS API GATEWAY module schema

## Properties

| Property   | Type                | Required | Description                                                                   |
|------------|---------------------|----------|-------------------------------------------------------------------------------|
| `flavor`   | string              | **Yes**  | Possible values are: `default`.                                               |
| `kind`     | string              | **Yes**  | Possible values are: `aws_api_gateway`.                                       |
| `metadata` | [object](#metadata) | **Yes**  |                                                                               |
| `spec`     | [object](#spec)     | **Yes**  |                                                                               |
| `version`  | string              | **Yes**  | Possible values are: `0.1`, `latest`.                                         |
| `advanced` | [object](#advanced) | No       |                                                                               |
| `out`      | [object](#out)      | No       | The output for your aws api gateway module, this can be generated or provided |

## advanced

### Properties

| Property          | Type                       | Required | Description                                            |
|-------------------|----------------------------|----------|--------------------------------------------------------|
| `aws_api_gateway` | [object](#aws_api_gateway) | No       | The advanced section of all the aws api gateway module |

### aws_api_gateway

The advanced section of all the aws api gateway module

#### Properties

| Property                                   | Type                                 | Required | Description                                                                                                                                                                    |
|--------------------------------------------|--------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `allowed_triggers`                         | [object](#allowed_triggers)          | No       | Map of allowed triggers to create Lambda permission                                                                                                                            |
| `api_key_selection_expression`             | string                               | No       | An API key selection expression. Valid values: $context.authorizer.usageIdentifierKey, $request.header.x-api-key.                                                              |
| `api_version`                              | string                               | No       | A version identifier for the API                                                                                                                                               |
| `authorizers`                              | [object](#authorizers)               | No       | Map of allowed triggers to create Lambda permission                                                                                                                            |
| `body`                                     | [object](#body)                      | No       | An OpenAPI specification that defines the set of routes and integrations to create as part of the HTTP APIs. Supported only for HTTP APIs.                                     |
| `cors`                                     | [object](#cors)                      | No       | The cross-origin resource sharing (CORS) configuration. Applicable for HTTP APIs.                                                                                              |
| `create_api_domain_name`                   | boolean                              | No       | Whether to create API domain name resource                                                                                                                                     |
| `create_default_stage_api_mapping`         | boolean                              | No       | Whether to create default stage API mapping                                                                                                                                    |
| `create_default_stage`                     | boolean                              | No       | Whether to create default stage                                                                                                                                                |
| `create_routes_and_integrations`           | boolean                              | No       | Whether to create routes and integrations resources                                                                                                                            |
| `create_vpc_link`                          | boolean                              | No       | Whether to create VPC links                                                                                                                                                    |
| `default_route_settings`                   | [object](#default_route_settings)    | No       | Settings for default route                                                                                                                                                     |
| `default_stage_access_log_destination_arn` | string                               | No       | Default stage's ARN of the CloudWatch Logs log group to receive access logs. Any trailing :* is trimmed from the ARN.                                                          |
| `default_stage_access_log_format`          | string                               | No       | Default stage's single line format of the access logs of data, as specified by selected $context variables.                                                                    |
| `disable_execute_api_endpoint`             | string                               | No       | Whether clients can invoke the API by using the default execute-api endpoint. To require that clients use a custom domain name to invoke the API, disable the default endpoint |
| `domain_name_certificate_arn`              | string                               | No       | The ARN of an AWS-managed certificate that will be used by the endpoint for the domain name                                                                                    |
| `domain_name`                              | string                               | No       | The domain name to use for API gateway                                                                                                                                         |
| `mutual_tls_authentication`                | [object](#mutual_tls_authentication) | No       | An Amazon S3 URL that specifies the truststore for mutual TLS authentication as well as version, keyed at uri and version                                                      |
| `route_key`                                | string                               | No       | Part of quick create. Specifies any route key. Applicable for HTTP APIs.                                                                                                       |
| `route_selection_expression`               | string                               | No       | he route selection expression for the API.                                                                                                                                     |
| `tags`                                     | [object](#tags)                      | No       | A mapping of tags to assign to API gateway resources.                                                                                                                          |
| `vpc_links`                                | [object](#vpc_links)                 | No       | Map of VPC Links details to create                                                                                                                                             |

#### allowed_triggers

Map of allowed triggers to create Lambda permission

##### Properties

| Property  | Type               | Required | Description                                                                                                                  |
|-----------|--------------------|----------|------------------------------------------------------------------------------------------------------------------------------|
| `^[a-zA-Z0-9_.-]*$` | [object](#trigger_properties) | No       | This is the name of the output interface this can be any name depending on the number of prefix domains in the domains block |

### trigger_properties

This is the name of the output interface this can be any name depending on the number of prefix domains in the domains block

###### Properties

| Property     | Type   | Required | Description                                                                                                                               |
|--------------|--------|----------|-------------------------------------------------------------------------------------------------------------------------------------------|
| `service`    | string | No       | The service you want to invoke from Possible values are: `apigateway`, `config.amazonaws.com`, `events.amazonaws.com`.                    |
| `source_arn` | string | No       | Set this to true if using Lambda@Edge, to enable publishing, limit the timeout, and allow edgelambda.amazonaws.com to invoke the function |

#### authorizers

Map of allowed triggers to create Lambda permission

##### Properties

| Property | Type            | Required | Description                                                                   |
|----------|-----------------|----------|-------------------------------------------------------------------------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#auth) | No       | These are the maps of all authorizers that can be attached to the api gateway |

##### auth

These are the maps of all authorizers that can be attached to the api gateway

###### Properties

| Property                            | Type   | Required | Description                                                                                                                                                                                                                                                                                |
|-------------------------------------|--------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `audience`                          | array  | No       | List of the intended recipients of the JWT. A valid JWT must provide an aud that matches at least one entry in this list                                                                                                                                                                   |
| `authorizer_credentials_arn`        | string | No       | Required credentials as an IAM role for API Gateway to invoke the authorizer. Supported only for REQUEST authorizers.                                                                                                                                                                      |
| `authorizer_payload_format_version` | string | No       | Format of the payload sent to an HTTP API Lambda authorizer. Required for HTTP API Lambda authorizers. Valid values: 1.0, 2.0. Possible values are: `1.0`, `2.0`.                                                                                                                          |
| `authorizer_result_ttl_in_seconds`  | string | No       | Time to live (TTL) for cached authorizer results, in seconds. If it equals 0, authorization caching is disabled. If it is greater than 0, API Gateway caches authorizer responses. The maximum value is 3600, or 1 hour. Defaults to 300. Supported only for HTTP API Lambda authorizers.  |
| `authorizer_type`                   | string | No       | Authorizer type. Valid values: JWT, REQUEST. Specify REQUEST for a Lambda function using incoming request parameters. For HTTP APIs, specify JWT to use JSON Web Tokens. Possible values are: `JWT`, `REQUEST`.                                                                            |
| `authorizer_uri`                    | string | No       | Authorizer's Uniform Resource Identifier (URI). For REQUEST authorizers this must be a well-formed Lambda function URI, such as the invoke_arn attribute of the aws_lambda_function resource. Supported only for REQUEST authorizers. Must be between 1 and 2048 characters in length.     |
| `enable_simple_responses`           | string | No       | Whether a Lambda authorizer returns a response in a simple format. If enabled, the Lambda authorizer can return a boolean value instead of an IAM policy. Supported only for HTTP APIs.                                                                                                    |
| `identity_sources`                  | string | No       | Identity sources for which authorization is requested. For REQUEST authorizers the value is a list of one or more mapping expressions of the specified request parameters. For JWT authorizers the single entry specifies where to extract the JSON Web Token (JWT) from inbound requests. |
| `issuer`                            | string | No       | Base domain of the identity provider that issues JSON Web Tokens, such as the endpoint attribute of the aws_cognito_user_pool resource.                                                                                                                                                    |
| `name`                              | string | No       | Name of the authorizer. Must be between 1 and 128 characters in length.                                                                                                                                                                                                                    |

#### body

An OpenAPI specification that defines the set of routes and integrations to create as part of the HTTP APIs. Supported only for HTTP APIs.

#### cors

The cross-origin resource sharing (CORS) configuration. Applicable for HTTP APIs.

##### Properties

| Property            | Type   | Required | Description                                                                |
|---------------------|--------|----------|----------------------------------------------------------------------------|
| `allow_credentials` | string | No       | Whether credentials are included in the CORS request.                      |
| `allow_headers`     | string | No       | Set of allowed HTTP headers.                                               |
| `allow_methods`     | string | No       | Set of allowed HTTP methods.                                               |
| `allow_origins`     | string | No       | Set of allowed origins.                                                    |
| `expose_headers`    | string | No       | Set of exposed HTTP headers.                                               |
| `max_age`           | string | No       | Number of seconds that the browser should cache preflight request results. |

#### default_route_settings

Settings for default route

##### Properties

| Property                   | Type    | Required | Description                                                                                                                                                                                                                                                                                                              |
|----------------------------|---------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `data_trace_enabled`       | boolean | No       | Whether data trace logging is enabled for the default route. Affects the log entries pushed to Amazon CloudWatch Logs. Defaults to false. Supported only for WebSocket APIs.                                                                                                                                             |
| `detailed_metrics_enabled` | boolean | No       | Whether detailed metrics are enabled for the route. Defaults to false                                                                                                                                                                                                                                                    |
| `logging_level`            | string  | No       | Logging level for the default route. Affects the log entries pushed to Amazon CloudWatch Logs. Valid values: ERROR, INFO, OFF. Defaults to OFF. Supported only for WebSocket APIs. Terraform will only perform drift detection of its value when present in a configuration Possible values are: `ERROR`, `INFO`, `OFF`. |
| `throttling_burst_limit`   | string  | No       | Throttling burst limit for the route.                                                                                                                                                                                                                                                                                    |
| `throttling_rate_limit`    | string  | No       | Throttling rate limit for the route.                                                                                                                                                                                                                                                                                     |

#### mutual_tls_authentication

An Amazon S3 URL that specifies the truststore for mutual TLS authentication as well as version, keyed at uri and version

##### Properties

| Property             | Type   | Required | Description                                                                                                                                                                                                                                                                                                                      |
|----------------------|--------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `truststore_uri`     | string | No       | Amazon S3 URL that specifies the truststore for mutual TLS authentication, for example, s3://bucket-name/key-name. The truststore can contain certificates from public or private certificate authorities. To update the truststore, upload a new version to S3, and then update your custom domain name to use the new version. |
| `truststore_version` | string | No       | Version of the S3 object that contains the truststore. To specify a version, you must have versioning enabled for the S3 bucket                                                                                                                                                                                                  |

#### tags

A mapping of tags to assign to API gateway resources.

#### vpc_links

Map of VPC Links details to create

##### Properties

| Property             | Type   | Required | Description                                                           |
|----------------------|--------|----------|-----------------------------------------------------------------------|
| `name`               | string | **Yes**  | Name of the VPC Link. Must be between 1 and 128 characters in length. |
| `security_group_ids` | string | **Yes**  | Security group IDs for the VPC Link.                                  |
| `subnet_ids`         | string | **Yes**  | Subnet IDs for the VPC Link.                                          |

## metadata

### Properties

| Property      | Type   | Required | Description                 |
|---------------|--------|----------|-----------------------------|
| `description` | string | **Yes**  | The description of the API. |

## out

The output for your aws api gateway module, this can be generated or provided

### Properties

| Property     | Type                  | Required | Description                                       |
|--------------|-----------------------|----------|---------------------------------------------------|
| `attributes` | [object](#attributes) | No       | The advanced section of all the aws lambda module |

### attributes

The advanced section of all the aws lambda module

#### Properties

| Property              | Type   | Required | Description                                                                                                                                       |
|-----------------------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| `arn`                 | string | No       | The ARN of the API                                                                                                                                |
| `endpoint`            | string | No       | The URI of the API                                                                                                                                |
| `execution_arn`       | string | No       | The ARN prefix to be used in an aws_lambda_permission's source_arn attribute or in an aws_iam_policy to authorize access to the @connections API. |
| `id`                  | string | No       | The API identifier                                                                                                                                |
| `stage_arn`           | string | No       | The default stage ARN                                                                                                                             |
| `stage_execution_arn` | string | No       | The ARN prefix to be used in an aws_lambda_permission's source_arn attribute or in an aws_iam_policy to authorize access to the @connections API. |
| `stage_id`            | string | No       | The default stage identifier                                                                                                                      |
| `stage_invoke_url`    | string | No       | The URL to invoke the API pointing to the stage                                                                                                   |
| `vpc_links_id`        | string | No       | The map of VPC Link identifiers                                                                                                                   |

## spec

### Properties

| Property       | Type                    | Required | Description                                                                               |
|----------------|-------------------------|----------|-------------------------------------------------------------------------------------------|
| `integrations` | [object](#integrations) | **Yes**  | Map of allowed triggers to create Lambda permission                                       |
| `protocol`     | string                  | **Yes**  | The API protocol. Valid values: HTTP, WEBSOCKET Possible values are: `WEBSOCKET`, `HTTP`. |

### integrations

Map of allowed triggers to create Lambda permission

#### Properties

| Property     | Type                  | Required | Description                                                                                 |
|--------------|-----------------------|----------|---------------------------------------------------------------------------------------------|
| `^(GET|POST|PUT|DELETE|PATCH|HEAD|OPTIONS|CONNECT|TRACE) .+$` | [object](#route_regex) | No       | This will have the HTTP method that is required for the rule and its path , eg:  `GET /path` |

#### route_regex

This will have the HTTP method that is required for the rule and its path , eg: `GET /path`

##### Properties

| Property                    | Type   | Required | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|-----------------------------|--------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `connection_id`             | string | No       | ID of the VPC link for a private integration. Supported only for HTTP APIs. Must be between 1 and 1024 characters in length                                                                                                                                                                                                                                                                                                                                                 |
| `connection_type`           | string | No       | Type of the network connection to the integration endpoint. Valid values: INTERNET, VPC_LINK. Default is INTERNET Possible values are: `VPC_LINK`, `INTERNET`.                                                                                                                                                                                                                                                                                                              |
| `content_handling_strategy` | string | No       | How to handle response payload content type conversions. Valid values: CONVERT_TO_BINARY, CONVERT_TO_TEXT. Supported only for WebSocket APIs Possible values are: `CONVERT_TO_BINARY`, `CONVERT_TO_TEXT`.                                                                                                                                                                                                                                                                   |
| `credentials_arn`           | string | No       | Credentials required for the integration, if any                                                                                                                                                                                                                                                                                                                                                                                                                            |
| `description`               | string | No       | The description of the API.                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| `integration_subtype`       | string | No       | AWS service action to invoke. Supported only for HTTP APIs when integration_type is AWS_PROXY. See the AWS service integration reference documentation for supported values. Must be between 1 and 128 characters in length.                                                                                                                                                                                                                                                |
| `integration_type`          | string | No       | Integration type of an integration. Valid values: AWS (supported only for WebSocket APIs), AWS_PROXY, HTTP (supported only for WebSocket APIs), HTTP_PROXY, MOCK (supported only for WebSocket APIs). For an HTTP API private integration, use HTTP_PROXY. Possible values are: `MOCK`, `AWS_PROXY`, `HTTP_PROXY`, `HTTP`.                                                                                                                                                  |
| `lambda_arn`                | string | No       | URI of the Lambda function for a Lambda proxy integration, when integration_type is AWS_PROXY. For an HTTP integration, specify a fully-qualified URL. For an HTTP API private integration, specify the ARN of an Application Load Balancer listener, Network Load Balancer listener, or AWS Cloud Map service.                                                                                                                                                             |
| `passthrough_behavior`      | string | No       | Pass-through behavior for incoming requests based on the Content-Type header in the request, and the available mapping templates specified as the request_templates attribute. Valid values: WHEN_NO_MATCH, WHEN_NO_TEMPLATES, NEVER. Default is WHEN_NO_MATCH. Supported only for WebSocket APIs Possible values are: `NEVER`, `WHEN_NO_TEMPLATES`, `WHEN_NO_MATCH`.                                                                                                       |
| `payload_format_version`    | string | No       | The format of the payload sent to an integration. Valid values: 1.0, 2.0. Default is 1.0 Possible values are: `0.1`, `0.2`.                                                                                                                                                                                                                                                                                                                                                 |
| `request_parameters`        | string | No       | For WebSocket APIs, a key-value map specifying request parameters that are passed from the method request to the backend. For HTTP APIs with a specified integration_subtype, a key-value map specifying parameters that are passed to AWS_PROXY integrations. For HTTP APIs without a specified integration_subtype, a key-value map specifying how to transform HTTP requests before sending them to the backend. See the Amazon API Gateway Developer Guide for details. |
| `timeout_milliseconds`      | string | No       | Custom timeout between 50 and 29,000 milliseconds for WebSocket APIs and between 50 and 30,000 milliseconds for HTTP APIs. The default timeout is 29 seconds for WebSocket APIs and 30 seconds for HTTP APIs. Terraform will only perform drift detection of its value when present in a configuration                                                                                                                                                                      |

