# Redis

## Schema Properties

|                                 | Type                                | Description                                                                                                                                                                    | Required   |
|:--------------------------------|:------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent           | string                              | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on                      | array                               | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled                        | boolean                             | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                          | enum: k8s, memorystore, elasticache |                                                                                                                                                                                | Yes        |
| kind                            | enum: redis                         |                                                                                                                                                                                | Yes        |
| lifecycle                       | enum: ENVIRONMENT_BOOTSTRAP         | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided                        | boolean                             | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version                         | enum: 0.1, 0.2, latest              |                                                                                                                                                                                | Yes        |
| metadata.name                   | string                              | Name of the resource                                                                                                                                                           | No         |
|                                 |                                     |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                            | object                              | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.authenticated              | boolean                             | Make this redis Password protected                                                                                                                                             | Yes        |
| spec.persistence_enabled        | boolean                             | Enable Persistence for this redis                                                                                                                                              | Yes        |
| spec.redis_version              | string                              | Version of redis e.g. 6.3                                                                                                                                                      | Yes        |
| spec.size.reader.instance_count | integer                             | The number of instances to create.                                                                                                                                             | Yes        |
| spec.size.reader.replica_count  | integer                             | [Deprecated] The number of instances to create.                                                                                                                                | No         |
| spec.size.writer.volume         | string                              | The size of the volume. e.g '10G' or '5Gi'                                                                                                                                     | No         |

## Outputs

|                                         | Type   | Description                    | Required   | Referencing                                                    |
|:----------------------------------------|:-------|:-------------------------------|:-----------|:---------------------------------------------------------------|
| out.interfaces.reader.username          | string | Username to connect (if any)   | No         | ${redis.RESOURCE_NAME.out.interfaces.reader.username}          |
| out.interfaces.reader.password          | string | Password to connect (if any)   | No         | ${redis.RESOURCE_NAME.out.interfaces.reader.password}          |
| out.interfaces.reader.host              | string | Host for service discovery     | No         | ${redis.RESOURCE_NAME.out.interfaces.reader.host}              |
| out.interfaces.reader.port              | string | Port for service discovery     | No         | ${redis.RESOURCE_NAME.out.interfaces.reader.port}              |
| out.interfaces.reader.name              | string | Name of interface, same as key | No         | ${redis.RESOURCE_NAME.out.interfaces.reader.name}              |
| out.interfaces.reader.connection_string | string | Connection string to connect   | No         | ${redis.RESOURCE_NAME.out.interfaces.reader.connection_string} |
| out.interfaces.writer.username          | string | Username to connect (if any)   | No         | ${redis.RESOURCE_NAME.out.interfaces.writer.username}          |
| out.interfaces.writer.password          | string | Password to connect (if any)   | No         | ${redis.RESOURCE_NAME.out.interfaces.writer.password}          |
| out.interfaces.writer.host              | string | Host for service discovery     | No         | ${redis.RESOURCE_NAME.out.interfaces.writer.host}              |
| out.interfaces.writer.port              | string | Port for service discovery     | No         | ${redis.RESOURCE_NAME.out.interfaces.writer.port}              |
| out.interfaces.writer.name              | string | Name of interface, same as key | No         | ${redis.RESOURCE_NAME.out.interfaces.writer.name}              |
| out.interfaces.writer.connection_string | string | Connection string to connect   | No         | ${redis.RESOURCE_NAME.out.interfaces.writer.connection_string} |

