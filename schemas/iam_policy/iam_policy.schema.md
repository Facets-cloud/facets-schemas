## Introduction
A AWS iam_policy level intent which can be used to create iam_policy in kubernetes clusters that can be mounted to deployments

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
|--------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)      | No       | The output for your aws IAM Policy  module, this can be generated or provided                                                                  |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

### Properties
| Property  | Type     | Required | Description                                                    |
|-----------|----------|----------|----------------------------------------------------------------|
| name      | string   | Yes      | Name property within the spec for the IAM Policy to be created |
| policy    | [object] | Yes      | Policy property                                                |




## advanced

Additional fields if any for a particular implementation of a resource

### Properties

| Property    | Type   | Required | Description                                   |
|-------------|--------|----------|-----------------------------------------------|
| description | string | No       | Description about the AWS IAM policy resource |
| name_prefix | string | No       | Name prefix for the IAM policy resource       |
| path        | string | No       | Path to save IAM policy resource              |
| tags        | object | No       | Tags for the IAM policy resource              |



## out

The output for your iam policy module, this can be generated or provided

| Property | Type   | Required | Description                                                  |
|----------|--------|----------|--------------------------------------------------------------|
| name     | string | Yes      | Name property within the spec for the IAM Policy got created |
| ARN      | string | Yes      | ARN of the generate IAM policy                               |


