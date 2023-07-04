# Introduction

AWS EKS managed Node Group implementation

## Advanced

The following variable is related to the AWS EKS Node Group resource and allows for more advanced configurations:

| Name | Description | Datatype | Required |
| --- | --- | --- | --- |
| eks.managed_node_group | Advanced values as per Terraform AWS EKS Node Group resource https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/eks_node_group<br>Note: if you want to pass more instance types you can use eks.managed_node_group.instance_types | map< string, any > | no |

# Example

```json
    {
  "advanced": {
    "eks": {
      "managed_node_group": {
        "desired_size": 2,
        "capacity_type": "ON_DEMAND",
        "ami_id": "",
        "tags": {
          "Purpose": "test"
        }
      }
    }
  }
}
```

