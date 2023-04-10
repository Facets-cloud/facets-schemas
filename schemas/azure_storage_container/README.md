## Introduction
A configmap level intent which can be used to create config map in kubernetes clusters that can be mounted to deployments

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
| `out`        | [object](#out)      | No       | The output for your configmap module, this can be generated or provided                                                                        |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

### Properties

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `data`   | [object](#data) | **Yes**  | the map of all filename and their contents |



## advanced

Additional fields if any for a particular implementation of a resource

### Properties

| Property | Type           | Required | Description                                           |
|----------|----------------|----------|-------------------------------------------------------|
| `k8s`    | [object](#k8s) | No       | The advanced section of all the k8s config map module |

### k8s

The advanced section of all the k8s config map module

| Property | Type | Required | Description |
|----------|------|----------|-------------|


## out

The output for your configmap module, this can be generated or provided

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### Flavors

* `config_map`