## Introduction
This document describes the structure of the AWS WAF schema.

### spec 

The `spec` object defines the configuration for Azure functions.

| Property         | Type                              | Description                                                                                 |
|------------------|-----------------------------------|---------------------------------------------------------------------------------------------|
| `scope`          | string                            | Specifies whether this is for an AWS CloudFront distribution or for a regional application. |
| `rule_groups`    | [rule_groups](#rule_groups)       | Queues configuration.                                                                       |
| `resource_arns`  | [resource_arns](#resource_arns)   | The Amazon Resource Name (ARN) of the resource to associate with the web ACL.               |
| `default_action` | [default_action](#default_action) | The action to perform if none of the rules contained in the WebACL match.                   |


#### resource_arns

The `resource_arns` specifies the Amazon Resource Name (ARN) of the resource to associate with the web ACL. Each `resource_arn` is defined with the structure below.'

| Property | Type   | Description                                     | Default | Required |
|----------|--------|-------------------------------------------------|---------|----------|
| `arn`    | string | The Amazon Resource Name (ARN) of the resource. | `null`  | Yes      |


#### rule_groups
       
The `rule_groups` specifies one or more rules groups to be added to the WAF resource. Each `rules` is defined with the structure below.'

| Property   | Type   | Description                                                                                                                                                                                | Default | Required |
|------------|--------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|----------|
| `arn`      | string | The Amazon Resource Name (ARN) of the rule_group resource.                                                                                                                                 | `null`  | Yes      |
| `priority` | number | If you define more than one Rule in a WebACL, AWS WAF evaluates each request against the rules in order based on the value of priority. AWS WAF processes rules with lower priority first. | `null`  | No       |


#### default_action

The `default_action` object defines the configuration if none of the rules contained in the WebACL match.


| Property | Type            | Description                                              | Default | Required |
|----------|-----------------|----------------------------------------------------------|---------|----------|
| `allow`  | [allow](#allow) | Specifies that AWS WAF should allow requests by default. | NA      | No       |
| `block`  | [block](#block) | Specifies that AWS WAF should block requests by default. | NA      | No       |

#### allow

The `allow` object defines the configuration that AWS WAF should allow requests by default.


| Property                  | Type                                                | Description                                  | Default | Required |
|---------------------------|-----------------------------------------------------|----------------------------------------------|---------|----------|
| `custom_request_handling` | [custom_request_handling](#custom_request_handling) | Defines custom handling for the web request. | `null`  | No       |


#### custom_request_handling

The `custom_request_handling` object defines the custom handling for the web request.


| Property        | Type                                 | Description                                                                | Default | Required |
|-----------------|--------------------------------------|----------------------------------------------------------------------------|---------|----------|
| `insert_header` | [insert_header](#custom_http_header) | The insert_header blocks used to define HTTP headers added to the request. | `null`  | Yes      |


#### block

The `block` object defines the configuration that AWS WAF should block requests by default.


| Property          | Type                                | Description                                    | Default | Required |
|-------------------|-------------------------------------|------------------------------------------------|---------|----------|
| `custom_response` | [custom_response](#custom_response) | Defines a custom response for the web request. | `null`  | No       |

#### custom_response

The `custom_response` object a custom response for the web request.


| Property                   | Type                                   | Description                                                                                | Default | Required |
|----------------------------|----------------------------------------|--------------------------------------------------------------------------------------------|---------|----------|
| `response_header`          | [response_header](#custom_http_header) | The response_header blocks used to define the HTTP response headers added to the response. | `null`  | No       |
| `custom_response_body_key` | string                                 | References the response body that you want AWS WAF to return to the web request client.    | `null`  | No       |
| `response_code`            | number                                 | The HTTP status code to return to the client.                                              | `null`  | Yes      |

#### custom_http_header

The `custom_http_header` block for passing custom headers.


| Property | Type   | Description                     | Default | Required |
|----------|--------|---------------------------------|---------|----------|
| `name`   | string | The name of the custom header.  | `null`  | Yes      |
| `value`  | string | The value of the custom header. | `null`  | Yes      |


## Outputs

| Property         | Type   | Description                |
|------------------|--------|----------------------------|
| `attributes.arn` | string | The ARN of the WAF WebACL. |
