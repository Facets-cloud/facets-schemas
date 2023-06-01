# Postgres

## Schema Properties

|                                 | Type                                     | Description                                                                                                                                                                    | Required   |
|:--------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                            | enum: postgres                           | Describes the type of resource. e.g. cloudsql or aurora                                                                                                                        | Yes        |
| flavor                          | enum: cloudsql, aurora                   | Implementation selector for the Postgres resource. e.g. for a resource type cloudsql or aurora.                                                                                | Yes        |
| version                         | enum: 0.1, latest                        | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| lifecycle                       | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                        | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent           | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                        | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                      | array                                    | Dependencies on other resources. e.g. application x may depend on y                                                                                                            | No         |
| metadata.name                   | string                                   | Name of the resource                                                                                                                                                           | No         |
|                                 |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                            | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                        | object                                   | Advanced values to be used by different flavours of postgres between Cloudsql or Aurora                                                                                        | No         |
| spec.postgres_version           | string                                   | Version of postgres e.g. 12.11                                                                                                                                                 | No         |
| spec.size.writer.volume         | string                                   | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                     | No         |
| spec.size.reader.instance_count | integer                                  | The number of instances to create.                                                                                                                                             | No         |
| spec.size.reader.replica_count  | integer                                  | [Deprecated] The number of instances to create.                                                                                                                                | No         |

## Outputs

|                                         | Type    | Description                                                                | Required   | Referencing                                                       |
|:----------------------------------------|:--------|:---------------------------------------------------------------------------|:-----------|:------------------------------------------------------------------|
| out                                     | object  | Output given by the resource for others to refer.                          | No         | ${postgres.RESOURCE_NAME.out}                                     |
| out.spec.postgres_version               | string  | Version of postgres e.g. 12.11                                             | No         | ${postgres.RESOURCE_NAME.out.spec.postgres_version}               |
| out.spec.size.writer.volume             | string  | The size of the volume. e.g '10G' or '5Gi'                                 | No         | ${postgres.RESOURCE_NAME.out.spec.size.writer.volume}             |
| out.spec.size.reader.instance_count     | integer | The number of instances to create.                                         | No         | ${postgres.RESOURCE_NAME.out.spec.size.reader.instance_count}     |
| out.spec.size.reader.replica_count      | integer | [Deprecated] The number of instances to create.                            | No         | ${postgres.RESOURCE_NAME.out.spec.size.reader.replica_count}      |
| out.interfaces.reader.username          | string  | Username to connect (if any)                                               | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.username}          |
| out.interfaces.reader.password          | string  | Password to connect (if any)                                               | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.password}          |
| out.interfaces.reader.host              | string  | Host for service discovery                                                 | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.host}              |
| out.interfaces.reader.port              | string  | Port for service discovery                                                 | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.port}              |
| out.interfaces.reader.name              | string  | Name of interface, same as key                                             | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.name}              |
| out.interfaces.reader.connection_string | string  | Connection string to connect                                               | No         | ${postgres.RESOURCE_NAME.out.interfaces.reader.connection_string} |
| out.interfaces.writer.username          | string  | Username to connect (if any)                                               | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.username}          |
| out.interfaces.writer.password          | string  | Password to connect (if any)                                               | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.password}          |
| out.interfaces.writer.host              | string  | Host for service discovery                                                 | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.host}              |
| out.interfaces.writer.port              | string  | Port for service discovery                                                 | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.port}              |
| out.interfaces.writer.name              | string  | Name of interface, same as key                                             | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.name}              |
| out.interfaces.writer.connection_string | string  | Connection string to connect                                               | No         | ${postgres.RESOURCE_NAME.out.interfaces.writer.connection_string} |
| out.postgresVersion                     | string  | Version of postgres e.g. 12.11                                             | No         | ${postgres.RESOURCE_NAME.out.postgresVersion}                     |
| out.excludedDatabases                   | list    | List of databases to be excluded for prometheus-postgres-exporter          | No         | ${postgres.RESOURCE_NAME.out.excludedDatabases}                   |
| out.defaultDatabase                     | string  | Default database for the connection string of prometheus-postgres-exporter | No         | ${postgres.RESOURCE_NAME.out.defaultDatabase}                     |

