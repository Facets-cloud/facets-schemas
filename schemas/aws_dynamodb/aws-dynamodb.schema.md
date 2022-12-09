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

| Property       | Type                    | Required | Description                                                                                                                                          |
|----------------|-------------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `aws_dynamodb` | object | No       | Advanced values as per Terraform AWS DynamoDB Module https://registry.terraform.io/modules/terraform-aws-modules/dynamodb-table/aws/3.1.1?tab=inputs |


## metadata

Metadata related to the resource

### Properties

| Property | Type   | Required | Description                                        |
|----------|--------|----------|----------------------------------------------------|
| `name`   | string | No       | Name of the resource                               |
|          |        |          |     - if not specified, fallback is the `filename` |

## out

Output given by the resource for others to refer.

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## spec

Specification as per resource types schema

### Properties

| Property     | Type                  | Required | Description                                                                            |
|--------------|-----------------------|----------|----------------------------------------------------------------------------------------|
| `attributes` | [object](#attributes) | No       | Map of attribute definitions. Only required for hash_key and range_key attributes      |
| `hash_key`   | string                | No       | The attribute to use as the hash (partition) key. Must also be defined as an attribute |

### attributes

Map of attribute definitions. Only required for hash_key and range_key attributes.

| Property | Type   | Required | Description                                            |
|----------|--------|----------|--------------------------------------------------------|
| `name`   | string | No       | The name of the attribute                              |
| `type`   | string | No       | The attribute type Possible values are: `S`, `N`, `B`. |

