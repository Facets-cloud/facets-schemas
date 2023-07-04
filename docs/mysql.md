# Mysql

## Schema Properties

|                                 | Type                                         | Description                                                                                                                                                                    | Required   |
|:--------------------------------|:---------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent           | string                                       | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on                      | array                                        | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled                        | boolean                                      | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                          | enum: aurora, cloudsql, rds, flexible_server | Implementation selector for the resource. e.g. for a resource type mysql.                                                                                                      | Yes        |
| kind                            | enum: mysql                                  | Describes the type of resource mysql. If not specified, fallback is the `folder_name`/instances                                                                                | Yes        |
| lifecycle                       | enum: ENVIRONMENT_BOOTSTRAP                  | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided                        | boolean                                      | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version                         | enum: 0.1, latest                            | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| metadata.name                   | string                                       | Name of the resource                                                                                                                                                           | No         |
|                                 |                                              |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                            | object                                       | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.mysql_version              | string                                       | Aurora/CloudSql/Rds compatible MySQL version (eg: 8.0.mysql_aurora.3.02.0, 8.0.31, 8.0)                                                                                        | Yes        |
| spec.size.reader.instance_count | integer                                      | The number of instances to create.                                                                                                                                             | Yes        |
| spec.size.reader.replica_count  | integer                                      | [Deprecated] The number of instances to create.                                                                                                                                | No         |
| spec.size.writer.volume         | string                                       | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                     | No         |

## Outputs

|                                         | Type   | Description                    | Required   | Referencing                                                    |
|:----------------------------------------|:-------|:-------------------------------|:-----------|:---------------------------------------------------------------|
| out.interfaces.reader.username          | string | Username to connect (if any)   | No         | ${mysql.RESOURCE_NAME.out.interfaces.reader.username}          |
| out.interfaces.reader.password          | string | Password to connect (if any)   | No         | ${mysql.RESOURCE_NAME.out.interfaces.reader.password}          |
| out.interfaces.reader.host              | string | Host for service discovery     | No         | ${mysql.RESOURCE_NAME.out.interfaces.reader.host}              |
| out.interfaces.reader.port              | string | Port for service discovery     | No         | ${mysql.RESOURCE_NAME.out.interfaces.reader.port}              |
| out.interfaces.reader.name              | string | Name of interface, same as key | No         | ${mysql.RESOURCE_NAME.out.interfaces.reader.name}              |
| out.interfaces.reader.connection_string | string | Connection string to connect   | No         | ${mysql.RESOURCE_NAME.out.interfaces.reader.connection_string} |
| out.interfaces.writer.username          | string | Username to connect (if any)   | No         | ${mysql.RESOURCE_NAME.out.interfaces.writer.username}          |
| out.interfaces.writer.password          | string | Password to connect (if any)   | No         | ${mysql.RESOURCE_NAME.out.interfaces.writer.password}          |
| out.interfaces.writer.host              | string | Host for service discovery     | No         | ${mysql.RESOURCE_NAME.out.interfaces.writer.host}              |
| out.interfaces.writer.port              | string | Port for service discovery     | No         | ${mysql.RESOURCE_NAME.out.interfaces.writer.port}              |
| out.interfaces.writer.name              | string | Name of interface, same as key | No         | ${mysql.RESOURCE_NAME.out.interfaces.writer.name}              |
| out.interfaces.writer.connection_string | string | Connection string to connect   | No         | ${mysql.RESOURCE_NAME.out.interfaces.writer.connection_string} |

