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
| `out`        | [object](#out)      | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## advanced

Additional fields if any for a particular implementation of a resource

### Properties

| Property | Type           | Required | Description                              |
|----------|----------------|----------|------------------------------------------|
| `aks`    | [object](#aks) | No       | Advanced Values for AKS nodepool         |
| `eks`    | [object](#eks) | No       | Advanced section of the EKS Self Managed |

### aks

Advanced Values for AKS nodepool

#### Properties

| Property    | Type                 | Required | Description                                                                                                                                       |
|-------------|----------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| `node_pool` | [object](#node_pool) | No       | aks values as per the documentation  https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/kubernetes_cluster_node_pool |

#### node_pool

aks values as per the documentation  https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/kubernetes_cluster_node_pool

##### Properties

| Property | Type              | Required | Description |
|----------|-------------------|----------|-------------|
| `values` | [object](#values) | No       |             |

##### values

###### Properties

| Property           | Type                        | Required | Description                         |
|--------------------|-----------------------------|----------|-------------------------------------|
| `kubelet_config`   | [object](#kubelet_config)   | No       | Block for all Linux configuration   |
| `linux_os_config`  | [object](#linux_os_config)  | No       | Block for all Linux configuration   |
| `tags`             | [object](#tags)             | No       | Tags required for the nodes created |
| `upgrade_settings` | [object](#upgrade_settings) | No       |                                     |

###### kubelet_config

Block for all Linux configuration

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### linux_os_config

Block for all Linux configuration

**Properties**

| Property        | Type                     | Required | Description                                                                       |
|-----------------|--------------------------|----------|-----------------------------------------------------------------------------------|
| `sysctl_config` | [object](#sysctl_config) | No       | Block for all System control configurations which should be under linux_os_config |

**sysctl_config**

Block for all System control configurations which should be under linux_os_config

| Property | Type | Required | Description |
|----------|------|----------|-------------|

###### tags

Tags required for the nodes created

**Properties**

| Property | Type   | Required | Description |
|----------|--------|----------|-------------|
| `test`   | string | No       |             |

###### upgrade_settings

**Properties**

| Property    | Type   | Required | Description |
|-------------|--------|----------|-------------|
| `max_surge` | string | **Yes**  |             |

### eks

Advanced section of the EKS Self Managed

#### Properties

| Property                  | Type                               | Required | Description                                                                                                                                                                                      |
|---------------------------|------------------------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `managed_node_group`      | [object](#managed_node_group)      | No       | Advanced values as per Terraform AWS EKS Managed Node Group resource https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/eks_node_group                                  |
|                           |                                    |          | Note: If you want to pass multiple intsnace types then you can use `instance_types` attribute in advanced along with spec.instance_type                                                          |
| `self_managed_node_group` | [object](#self_managed_node_group) | No       | Advanced values as per Terraform AWS EKS Self Managed Node Group module https://registry.terraform.io/modules/terraform-aws-modules/eks/aws/latest/submodules/self-managed-node-group?tab=inputs |

#### managed_node_group

Advanced values as per Terraform AWS EKS Managed Node Group resource https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/eks_node_group 
Note: If you want to pass multiple intsnace types then you can use `instance_types` attribute in advanced along with spec.instance_type

| Property | Type | Required | Description |
|----------|------|----------|-------------|

#### self_managed_node_group

Advanced values as per Terraform AWS EKS Self Managed Node Group module https://registry.terraform.io/modules/terraform-aws-modules/eks/aws/latest/submodules/self-managed-node-group?tab=inputs

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## metadata

Metadata related to the resource

### Properties

| Property | Type   | Required | Description                                        |
|----------|--------|----------|----------------------------------------------------|
| `name`   | string | No       | Name of the resource                               |
|          |        |          |     - if not specified, fallback is the `filename` |

## out

Output given by the resource for others to refer.

### Properties

| Property | Type              | Required | Description |
|----------|-------------------|----------|-------------|
| `labels` | [object](#labels) | No       |             |
| `name`   | string            | No       |             |
| `spec`   | [object](#spec)   | No       |             |
| `taints` | list              | No       |             |

### labels

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### spec

#### Properties

| Property         | Type              | Required | Description                                                                                           |
|------------------|-------------------|----------|-------------------------------------------------------------------------------------------------------|
| `azs`            | string            | **Yes**  | Comma separated string of one or more availability zones for the Node Pool                            |
| `disk_size`      | number            | **Yes**  | Disk size in GiB for worker nodes                                                                     |
| `instance_type`  | string            | **Yes**  | Instance type associated with the Node Pool                                                           |
| `is_public`      | boolean           | **Yes**  | Whether to launch nodes in Public or Private network                                                  |
| `labels`         | [object](#labels) | **Yes**  | Key-value map of Kubernetes labels                                                                    |
| `max_node_count` | number            | **Yes**  | Maximum number of worker nodes                                                                        |
| `min_node_count` | number            | **Yes**  | Minimum number of worker nodes                                                                        |
| `taints`         | [object](#taints) | **Yes**  | The Kubernetes taints to be applied to the nodes in the Node Pool. Maximum of 50 taints per Node Pool |

#### taints

The Kubernetes taints to be applied to the nodes in the Node Pool. Maximum of 50 taints per Node Pool

| Property | Type   | Required | Description                                                                                 |
|----------|--------|----------|---------------------------------------------------------------------------------------------|
| `effect` | string | No       | The effect of the taint Possible values are: `NoSchedule`, `NoExecute`, `PreferNoSchedule`. |
| `key`    | string | No       | The key of the taint                                                                        |
| `value`  | string | No       | The value of the taint                                                                      |

## spec

Specification as per resource types schema

### Properties

| Property         | Type              | Required | Description                                                                                           |
|------------------|-------------------|----------|-------------------------------------------------------------------------------------------------------|
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

