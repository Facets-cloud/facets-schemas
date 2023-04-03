# Introduction

Nodepool intent to deploy nodepools for kubernetes cluster. It supports all threee cloud providers (AWS, Azure, GCP)

## Properties

| Property     | Type            | Required | Description                                                                                                                                    |
|--------------|-----------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string          | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string          | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | object          | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec) | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string          | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | object          | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                 | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean         | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string          | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)  | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean         | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

### Properties

| Property         | Type              | Required | Description                                                                                           |
| ---------------- | ----------------- | -------- | ----------------------------------------------------------------------------------------------------- |
| `azs`            | string            | **Yes**  | Comma separated string of one or more availability zones for the Node Pool                            |
| `disk_size`      | number            | **Yes**  | Disk size in GiB for worker nodes                                                                     |
| `instance_type`  | string            | **Yes**  | Instance type associated with the Node Pool                                                           |
| `is_public`      | boolean           | **Yes**  | Whether to launch nodes in Public or Private network                                                  |
| `labels`         | [object](#labels) | **Yes**  | Key-value map of Kubernetes labels                                                                    |
| `max_node_count` | number            | **Yes**  | Maximum number of worker nodes                                                                        |
| `min_node_count` | number            | **Yes**  | Minimum number of worker nodes                                                                        |
| `taints`         | [object](#taints) | **Yes**  | The Kubernetes taints to be applied to the nodes in the Node Pool. Maximum of 50 taints per Node Pool |

### taints

The Kubernetes taints to be applied to the nodes in the Node Pool. Maximum of 50 taints per Node Pool

| Property | Type   | Required | Description                                                                                 |
|----------|--------|----------|---------------------------------------------------------------------------------------------|
| `effect` | string | No       | The effect of the taint Possible values are: `NoSchedule`, `NoExecute`, `PreferNoSchedule`. |
| `key`    | string | No       | The key of the taint                                                                        |
| `value`  | string | No       | The value of the taint                                                                      |


## out

Output given by the resource for others to refer.

### Properties

| Property | Type            | Required | Description |
|----------|-----------------|----------|-------------|
| `labels` | object          | No       |             |
| `name`   | string          | No       |             |
| `spec`   | [object](#spec) | No       |             |
| `taints` | list            | No       |             |


### flavor

- [aks_nodepool](nodepool.aks.schema.md)
- [eks-managed](nodepool.eks-managed.schema.md)
- [eks-self-managed](nodepool.eks-self-managed.schema.md)