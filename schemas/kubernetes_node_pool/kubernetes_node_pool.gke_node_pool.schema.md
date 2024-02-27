# Introduction

Google gke_node_pool implementation

## Advanced


The following variables are related to the configuration of an GKE node pool:

| Name             | Description                                                                                                                                                                                                         | Datatype | Required |
|------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|----------|
| upgrade_settings | [Block](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/container_node_pool) Specify node upgrade settings to change how GKE upgrades nodes. The maximum number of nodes upgraded simultaneously is limited to 20                                               |   map     | No      |
| node_config   | [Block](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/container_node_pool) for all node configuration of the nodes in the node pool                                               | map      | No       |
| network_config  | [Block](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/container_node_pool) The network configuration of the pool. Such as configuration for Adding Pod IP address ranges to the node pool. Or enabling private nodes                                                 | map      | No       |
| management    | [Block](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/container_node_pool) Node management configuration, wherein auto-repair and auto-upgrade is configured. | map      | No       |
| tags             | Mapping of tags to assign to the node pool resource.                                                                                                                                                                | map      | No       |

# Example

```json
{
  "advanced": {
    "gke": {
        "upgrade_settings": {
            "max_surge": 1,
            "max_unavailable": 0
        }
    }
  }
}

```