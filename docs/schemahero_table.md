# Schemahero Table

## Schema Properties

|                                          | Type                        | Description                                                                                                                                                                    | Required   |
|:-----------------------------------------|:----------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent                    | string                      | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on                               | array                       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled                                 | boolean                     | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                                   | enum: default               | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                     | Yes        |
| kind                                     | string                      | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                | Yes        |
| lifecycle                                | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided                                 | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version                                  | enum: 0.1                   | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| metadata.name                            | string                      | Name of the resource                                                                                                                                                           | Yes        |
| spec                                     | object                      | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.columns.KEY.attributes              | object                      | Adding attributes to the column                                                                                                                                                | No         |
| spec.columns.KEY.constraints             | object                      | Adding constraints to the table                                                                                                                                                | No         |
| spec.columns.KEY.name                    | string                      | Name of the column                                                                                                                                                             | Yes        |
| spec.columns.KEY.type                    | string                      | Data type of the column                                                                                                                                                        | Yes        |
| spec.connection                          | enum: mysql                 | Database connection type                                                                                                                                                       | No         |
| spec.database                            | string                      | Name of the schemahero database object                                                                                                                                         | No         |
| spec.foreign_keys.KEY.columns            | array                       | Columns in the foreign key                                                                                                                                                     | Yes        |
| spec.foreign_keys.KEY.references.columns | array                       | Foreign table columns                                                                                                                                                          | Yes        |
| spec.foreign_keys.KEY.references.table   | string                      | Table name of the reference table                                                                                                                                              | Yes        |
| spec.indexes.KEY.columns                 | array                       | Define columns in the index                                                                                                                                                    | Yes        |
| spec.indexes.KEY.is_unique               | boolean                     | Bool indicating a unique index                                                                                                                                                 | Yes        |
| spec.indexes.KEY.name                    | string                      | Name of the index                                                                                                                                                              | Yes        |
| spec.primary_key                         | array                       | List of column names as the primary key of the table                                                                                                                           | No         |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

