# Introduction

AWS EKS managed Node Group implementation

## Advanced

The following variable is related to the AWS EKS Node Group resource and allows for more advanced configurations:

| Name        | Description                                                                                                                                                                                                                                                        | Datatype           | Required |
| ----------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ------------------ | -------- |
| eks_managed | Advanced values as per Terraform AWS EKS Node Group resource https://registry.terraform.io/modules/terraform-aws-modules/eks/aws/18.30.3/submodules/eks-managed-node-group<br>Note: if you want to pass more instance types you can use eks_managed.instance_types | map< string, any > | no       |

# Example

```json
{
  "advanced": {
    "eks_managed": {
      "desired_size": 2,
      "capacity_type": "ON_DEMAND",
      "ami_id": "",
      "tags": {
        "Purpose": "test"
      }
    }
  }
}
```
