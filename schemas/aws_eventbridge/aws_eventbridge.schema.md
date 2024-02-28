# Introduction

This module creates aws eventbridge rules and targets.

## Properties

| Property     | Type            | Required | Description                                                                                                                                    |
| ------------ | --------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| `flavor`     | string          | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string          | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | object          | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec) | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string          | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | object          | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                 | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean         | No       | Flag to disable the resource                                                                                          
| `out`        | [object](#out)  | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean         | No       | Flag to tell if the resource should not be provisioned by facets   |

## Spec

Specification as per resource types schema

### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `rules` | [object](#rules) | **Yes**  |  The event rules to be created. |

## Rules

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `is_enabled` | boolean | **No**  |  Flag to enable disable the rule. |
| `schedule_expression` | string | **No**  |  The schedule for the rule. |
| `description` | string | **No**  |  The description for the rule. |
| `targets` | [list(object)](#targets) | **No**  |  The list of targets. |

## Targets

EventBridge targets

### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `arn` | string | **No**  |  The arn of the target |
| `input` | object | **No**  | Valid JSON text passed to the target  |

## Advanced

Advanced specifications 

### Properties

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `event_bus_name` | string | **No** | The name or ARN of the event bus to associate with the rule. If you omit this, the default event bus is used.           |


## Output 

### Properties

| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
|`rule_id`    | string | **No** | Event Rule ID                                          |
|`rule_arn`   | srting | **No** | Event Rule ARN                                         |
|`rule_name`  | string | **No** | Event Rule name                                        |
