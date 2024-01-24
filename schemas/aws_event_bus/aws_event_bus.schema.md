# Introduction
Event buses receive events from a variety of sources and match them to rules in your account.

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
| `name` | string | **No**  | The name of the event bus to be created.        |

## Advanced

Advanced specifications 

| Property           | Type   | Required | Description                                                                                                                                                                                                                              |
| ------------------ | ------ | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `additional_tags`     | object | **No**  | A map of tags to assign to the event bus. |

## Output 
| Name            | Description                                                          | Datatype | Required |
|-----------------|----------------------------------------------------------------------|----------|----------|
| `name`  | The name of the event bus | string      | yes      |