# Kubernetes Node Pool

## Schema Properties

|                        | Type                                          | Description                                                                                                                                                                    | Required   |
|:-----------------------|:----------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent  | string                                        | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on             | array                                         | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled               | boolean                                       | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                 | enum: eks_self_managed, aks, eks_managed      |                                                                                                                                                                                | Yes        |
| kind                   | enum: kubernetes_node_pool                    |                                                                                                                                                                                | Yes        |
| lifecycle              | enum: ENVIRONMENT_BOOTSTRAP                   | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided               | boolean                                       | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version                | enum: 0.1, latest                             |                                                                                                                                                                                | Yes        |
| metadata.name          | string                                        | Name of the resource                                                                                                                                                           | No         |
|                        |                                               |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                   | object                                        | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.azs               | string                                        | Comma separated string of one or more availability zones for the Node Pool                                                                                                     | Yes        |
| spec.disk_size         | number                                        | Disk size in GiB for worker nodes                                                                                                                                              | Yes        |
| spec.instance_type     | string                                        | Instance type associated with the Node Pool                                                                                                                                    | Yes        |
| spec.is_public         | boolean                                       | Whether to launch nodes in Public or Private network                                                                                                                           | Yes        |
| spec.labels            | object                                        | Key-value map of Kubernetes labels                                                                                                                                             | Yes        |
| spec.max_node_count    | number                                        | Maximum number of worker nodes                                                                                                                                                 | Yes        |
| spec.min_node_count    | number                                        | Minimum number of worker nodes                                                                                                                                                 | Yes        |
| spec.taints.KEY.effect | enum: NoSchedule, NoExecute, PreferNoSchedule | The effect of the taint                                                                                                                                                        | No         |
| spec.taints.KEY.key    | string                                        | The key of the taint                                                                                                                                                           | No         |
| spec.taints.KEY.value  | string                                        | The value of the taint                                                                                                                                                         | No         |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

