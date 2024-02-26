# Introduction

Azure AKS Node Group implementation

## Advanced


The following variables are related to the configuration of an AKS node pool:

| Name             | Description                                                                                                                                                                                                         | Datatype | Required |
|------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|----------|
| upgrade_settings | [Block](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/kubernetes_cluster_node_pool) for all upgrade configuration for the node pool                                               |          | Yes      |
| kubelet_config   | [Block](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/kubernetes_cluster_node_pool) for all kubelet configuration for the node pool                                               | map      | No       |
| linux_os_config  | [Block](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/kubernetes_cluster_node_pool) for all Linux configuration for the node pool                                                 | map      | No       |
| sysctl_config    | [Block](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/kubernetes_cluster_node_pool) for all system control configurations which should be under linux_os_config for the node pool | map      | No       |
| tags             | Mapping of tags to assign to the node pool resource.                                                                                                                                                                | map      | No       |


# Example

```json
{
  "advanced": {
    "aks": {
      "node_pool": {
        
      }
    }
  }
}
```

