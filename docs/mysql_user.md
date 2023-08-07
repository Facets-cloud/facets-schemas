# Mysql User

## Schema Properties

|                                 | Type                        | Description                                                                                                                                                                    | Required   |
|:--------------------------------|:----------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent           | string                      | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on                      | array                       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled                        | boolean                     | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                          | enum: default               | Implementation selector for the postgres_user resource. e.g. for a resource type default                                                                                       | Yes        |
| kind                            | enum: mysql_user            | Describes the type of resource.                                                                                                                                                | Yes        |
| lifecycle                       | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided                        | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version                         | enum: 0.1, latest           | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| metadata.name                   | string                      | Name of the resource                                                                                                                                                           | No         |
|                                 |                             |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                            | object                      | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.endpoint                   | string                      | The MySQL database endpoint. Syntax: `{username}:{password}@{endpoint}:{port}`, Eg: `mysql:testMySQLPassword@test-mysql.default.svc.cluster.local:3306`                        | Yes        |
| spec.permissions.KEY.database   | string                      | The database name. Note: You can use wildcard `*` for ALL databases                                                                                                            | Yes        |
| spec.permissions.KEY.permission | enum: ADMIN, RWO, RO        | The canned permission for grant                                                                                                                                                | Yes        |
| spec.permissions.KEY.table      | string                      | The table name. Note: You can use wildcard `*` for ALL tables                                                                                                                  | Yes        |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

