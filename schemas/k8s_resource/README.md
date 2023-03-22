## Introduction
A k8s_resource level intent which can be used to create any kubernetes resource by converting the kubernetes manifest YAML to JSON

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
| `resource`   | object | **Yes**  | This should contain the converted Kubernetes YAML manifest to JSON, You can only pass one manifest per resource. If you need to create multiple resources, you need multiple json files |



## advanced

Additional fields if any for a particular implementation of a resource

### Properties

| Property | Type           | Required | Description                                           |
|----------|----------------|----------|-------------------------------------------------------|
| `k8s_resource`    | [object](#k8s_resource) | No       | The advanced section of the k8s_resource module |

### k8s_resource

The advanced section of the k8s_resource module

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `timeout`   | integer | **No**  | Timeout for the resource |
| `cleanup_on_fail`   | boolean | **No**  | Whether to clean up when the resource installation fails|
| `wait`   | boolean | **No**  | Whether to wait until all the resources has been successfully created |


## out

Output given by the resource for others to refer.

| Property     | Type                  | Required | Description                                                           |
|--------------|-----------------------|----------|-----------------------------------------------------------------------|
| `interfaces` | [object](#interfaces) | No       | The output for your k8s_resource module, this can be generated or provided |

### Flavors

* `default`