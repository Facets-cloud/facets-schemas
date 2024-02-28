## Introduction
kubernetes_secret intent can be used to create kubernetes_secret resource by converting the kubernetes manifest YAML to JSON

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
|--------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress it can be default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, kubernetes_secret, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                                               |
| `depends_on` | string              | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                     |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets      |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                         |

## spec

Specification as per resource types schema

### Properties

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `data`   | object | **Yes**  | This should contain the kubernetes_secret and their respective data in base64 encocded format |

## advanced

Additional fields if any for a particular implementation of a resource

### Properties

| Property | Type           | Required | Description                                           |
|----------|----------------|----------|-------------------------------------------------------|
| `k8s`    | [object](#kubernetes_secret) | No       | The advanced section of the kubernetes_secret module |

### k8s

The advanced section of the kubernetes_secret module

| Property | Type            | Required | Description                                |
|----------|-----------------|----------|--------------------------------------------|
| `timeout`   | integer | **No**  | Timeout for the resource |
| `cleanup_on_fail`   | boolean | **No**  | Whether to clean up when the resource installation fails|
| `wait`   | boolean | **No**  | Whether to wait until all the resources has been successfully created |