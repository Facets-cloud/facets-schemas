# Introduction

AWS EKS self-managed Node Group implementation

## Advanced

The following variable is related to the AWS EKS Node Group resource and allows for more advanced configurations:

| Name                        | Description                                                                                                                                                                           | Datatype           | Required |
|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|----------|
| eks.self_managed_node_group | Advanced values as per Terraform AWS EKS Node Group resource https://registry.terraform.io/modules/terraform-aws-modules/eks/aws/latest/submodules/self-managed-node-group?tab=inputs | map< string, any > | no       |

# Example

```json
    {
    "advanced": {
      "eks": {
        "self_managed_node_group": {}
      }
    }
}
```

