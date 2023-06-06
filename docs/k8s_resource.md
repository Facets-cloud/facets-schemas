# K8S Resource

## Schema Properties

|                       | Type                        | Description                                                                                                                                                                    | Required   |
|:----------------------|:----------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent | string                      | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on            | array                       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled              | boolean                     | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                | enum: default               |                                                                                                                                                                                | Yes        |
| kind                  | enum: k8s_resource          |                                                                                                                                                                                | Yes        |
| lifecycle             | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided              | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version               | enum: 0.1, latest           |                                                                                                                                                                                | Yes        |
| metadata.name         | string                      | Name of the resource                                                                                                                                                           | No         |
|                       |                             |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                      | Specification as per resource types schema                                                                                                                                     | Yes        |
| spec.resource         | object                      | This should contain the kubernetes YAML manifest converted to JSON and pasted in a block                                                                                       | Yes        |

## Outputs

|                                      | Type   | Description                    | Required   | Referencing                                                        |
|:-------------------------------------|:-------|:-------------------------------|:-----------|:-------------------------------------------------------------------|
| out.interfaces.KEY.username          | string | Username to connect (if any)   | No         | ${k8s_resource.RESOURCE_NAME.out.interfaces.KEY.username}          |
| out.interfaces.KEY.password          | string | Password to connect (if any)   | No         | ${k8s_resource.RESOURCE_NAME.out.interfaces.KEY.password}          |
| out.interfaces.KEY.host              | string | Host for service discovery     | No         | ${k8s_resource.RESOURCE_NAME.out.interfaces.KEY.host}              |
| out.interfaces.KEY.port              | string | Port for service discovery     | No         | ${k8s_resource.RESOURCE_NAME.out.interfaces.KEY.port}              |
| out.interfaces.KEY.name              | string | Name of interface, same as key | No         | ${k8s_resource.RESOURCE_NAME.out.interfaces.KEY.name}              |
| out.interfaces.KEY.connection_string | string | Connection string to connect   | No         | ${k8s_resource.RESOURCE_NAME.out.interfaces.KEY.connection_string} |

