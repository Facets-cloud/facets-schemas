# Mongo

## Schema Properties

|                          | Type                        | Description                                                                                                                                                                    | Required   |
|:-------------------------|:----------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent    | string                      | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on               | array                       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled                 | boolean                     | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                   | enum: k8s                   | Implementation selector for the Mongo resource. e.g. for a resource type k8s                                                                                                   | Yes        |
| kind                     | enum: mongo                 | Describes the type of resource.                                                                                                                                                | Yes        |
| lifecycle                | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided                 | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version                  | enum: 0.2, latest           | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| metadata.name            | string                      | Name of the resource                                                                                                                                                           | No         |
|                          |                             |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                     | object                      | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.authenticated       | boolean                     | Make this mongodb instance Password protected                                                                                                                                  | Yes        |
| spec.mongo_version       | string                      | Version of mongodb e.g. 6.0, see valid tags here: https://hub.docker.com/_/mongo/tags?page=1                                                                                   | Yes        |
| spec.size.instance_count | integer                     | The number of instances to create.                                                                                                                                             | No         |
| spec.size.replica_count  | integer                     | [Deprecated] The number of instances to create.                                                                                                                                | No         |

## Outputs

|                                          | Type   | Description                                                                                                                                | Required   | Referencing                                                     |
|:-----------------------------------------|:-------|:-------------------------------------------------------------------------------------------------------------------------------------------|:-----------|:----------------------------------------------------------------|
| out.interfaces.cluster.endpoint          | string | A comma-separated string of endpoints in the format 'host1:port1,host2:port2'. The number of endpoints must be greater than or equal to 1. | No         | ${mongo.RESOURCE_NAME.out.interfaces.cluster.endpoint}          |
| out.interfaces.cluster.username          | string | The username to use for authentication when connecting to the datastore.                                                                   | No         | ${mongo.RESOURCE_NAME.out.interfaces.cluster.username}          |
| out.interfaces.cluster.password          | string | The password to use for authentication when connecting to the datastore.                                                                   | No         | ${mongo.RESOURCE_NAME.out.interfaces.cluster.password}          |
| out.interfaces.cluster.connection_string | string | The connection_string to use when connecting to the datastore.                                                                             | No         | ${mongo.RESOURCE_NAME.out.interfaces.cluster.connection_string} |

