# Postgres

## Schema Properties

|                                 | Type                        | Description                                                                                                                                                                    | Required   |
|:--------------------------------|:----------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent           | string                      | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on                      | array                       | Dependencies on other resources. e.g. application x may depend on y                                                                                                            | No         |
| disabled                        | boolean                     | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                          | enum: cloudsql, aurora      | Implementation selector for the Postgres resource. e.g. for a resource type cloudsql or aurora.                                                                                | Yes        |
| kind                            | enum: postgres              | Describes the type of resource. e.g. cloudsql or aurora                                                                                                                        | Yes        |
| lifecycle                       | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided                        | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version                         | enum: 0.1, latest           | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| metadata.name                   | string                      | Name of the resource                                                                                                                                                           | No         |
|                                 |                             |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                            | object                      | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.postgres_version           | string                      | Version of postgres e.g. 12.11                                                                                                                                                 | Yes        |
| spec.size.reader.instance_count | integer                     | The number of instances to create.                                                                                                                                             | Yes        |
| spec.size.reader.replica_count  | integer                     | [Deprecated] The number of instances to create.                                                                                                                                | No         |
| spec.size.writer.volume         | string                      | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                     | No         |

## Outputs

|                                         | Type   | Description                    | Required   | Referencing                                                       |
|:----------------------------------------|:-------|:-------------------------------|:-----------|:------------------------------------------------------------------|
| out.interfaces.reader.username          | string | Username to connect (if any)   | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.username}          |
| out.interfaces.reader.password          | string | Password to connect (if any)   | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.password}          |
| out.interfaces.reader.host              | string | Host for service discovery     | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.host}              |
| out.interfaces.reader.port              | string | Port for service discovery     | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.port}              |
| out.interfaces.reader.name              | string | Name of interface, same as key | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.name}              |
| out.interfaces.reader.connection_string | string | Connection string to connect   | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.connection_string} |
| out.interfaces.writer.username          | string | Username to connect (if any)   | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.username}          |
| out.interfaces.writer.password          | string | Password to connect (if any)   | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.password}          |
| out.interfaces.writer.host              | string | Host for service discovery     | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.host}              |
| out.interfaces.writer.port              | string | Port for service discovery     | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.port}              |
| out.interfaces.writer.name              | string | Name of interface, same as key | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.name}              |
| out.interfaces.writer.connection_string | string | Connection string to connect   | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.connection_string} |

