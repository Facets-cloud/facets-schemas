# Introduction

Node Fleet implementation

## Advanced


The following variables are related to the configuration of an Node Fleet:

| Name             | Description                                                                                                                                                                                                         | Datatype | Required |
|------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|----------|
| eks | Advanced Values for EKS nodepool                                              | map      | No      |
| aks | Advanced Values for AKS nodepool                                              | map      | No       |
| gke | Advanced Values for AKS nodepool                                              | map      | No       |

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
