---
title: "Redis elasticache flavor"
slug: "redis-elasticache-flavor"
hidden: true
createdAt: "2022-10-12T05:41:05.883Z"
updatedAt: "2022-10-12T06:11:46.471Z"
---
This is the implementation to create an Elasticache database with cluster mode disabled.

Terraform resources used:

- [aws_elasticache_replication_group](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_replication_group)
- [aws_elasticache_parameter_group](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_parameter_group)
- [aws_security_group](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group)
- [aws_elasticache_subnet_group](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_subnet_group)

## JSON values

[block:parameters]
{
  "data": {
    "h-0": "Key",
    "h-1": "Optional",
    "h-2": "Type",
    "h-3": "Values",
    "0-0": "`flavor`",
    "0-1": "No",
    "0-2": "string",
    "0-3": "`elasticache`",
    "1-0": "`kind`",
    "1-1": "No",
    "1-2": "string",
    "1-3": "`redis`",
    "2-0": "`version`",
    "2-1": "No",
    "2-2": "string",
    "2-3": "`0.1`  \nDefault value - `latest`.",
    "3-0": "`disabled`",
    "3-1": "Yes",
    "3-2": "boolean",
    "3-3": "`true`, `false`  \nDefault value - `true`",
    "4-0": "`provided`",
    "4-1": "Yes",
    "4-2": "boolean",
    "4-3": "`true`, `false`  \nDefault value - `false`",
    "5-0": "`depends_on`",
    "5-1": "Yes",
    "5-2": "array",
    "5-3": "Default value - `[]`",
    "6-0": "`metadata`",
    "6-1": "No",
    "6-2": "object",
    "6-3": "See [metadata](https://readme.facets.cloud/docs/redis-intent#metadata).",
    "7-0": "`spec`",
    "7-1": "No",
    "7-2": "object",
    "7-3": "See [spec](https://readme.facets.cloud/docs/redis-intent#spec).",
    "8-0": "`out`",
    "8-1": "Yes",
    "8-2": "object",
    "8-3": "See [out](https://readme.facets.cloud/docs/redis-intent#out)."
  },
  "cols": 4,
  "rows": 9,
  "align": [
    "left",
    "left",
    "left",
    "left"
  ]
}
[/block]

## Advanced Schema

[Redis elasticache flavor schema](https://facets-cloud.github.io/facets-schemas/schemas/redis/flavor-elasticache.schema.json)

### Advanced values

| Key                                             | Optional | Type | Description                                                                                                                                                                                               |
| :---------------------------------------------- | :------- | :--- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `elasticache.aws_elasticache_replication_group` | Yes      | Map  | Cluster values for this resource as per the <https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_replication_group>.                                                  |
| `elasticache.aws_elasticache_parameter_group`   | Yes      | Map  | Parameter group values for this resource as per the [elasticache_parameter_group documentation](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_parameter_group). |
| `elasticache.aws_security_group`                | Yes      | Map  | Security group values for this resource as per the [security_group documentation](https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/security_group).                            |