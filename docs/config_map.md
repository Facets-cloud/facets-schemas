# Config Map

## Schema Properties

|                                      | Type                                     | Description                                                                                                                                                                    | Required   |
|:-------------------------------------|:-----------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| kind                                 | enum: config_map                         |                                                                                                                                                                                | Yes        |
| flavor                               | enum: default                            |                                                                                                                                                                                | Yes        |
| version                              | enum: 0.1, latest                        |                                                                                                                                                                                | Yes        |
| lifecycle                            | enum: ENVIRONMENT, ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT` or `ENVIRONMENT_BOOTSTRAP`)                                                              | No         |
| disabled                             | boolean                                  | Flag to disable the resource                                                                                                                                                   | No         |
| conditional_on_intent                | string                                   | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| provided                             | boolean                                  | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| depends_on                           | array                                    | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| metadata.name                        | string                                   | Name of the resource                                                                                                                                                           | No         |
|                                      |                                          |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                                 | object                                   | Specification as per resource types schema                                                                                                                                     | Yes        |
| advanced                             | object                                   | Additional fields if any for a particular implementation of a resource                                                                                                         | No         |
| spec.data.DATUM_NAME.DATUM_NAME_NAME | string                                   | The contents of the file                                                                                                                                                       | No         |
| advanced.k8s                         | object                                   | The advanced section of all the k8s config map module                                                                                                                          | No         |

## Outputs

|                            | Type   | Description                                                                                                                  | Required   | Referencing                                            |
|:---------------------------|:-------|:-----------------------------------------------------------------------------------------------------------------------------|:-----------|:-------------------------------------------------------|
| out                        | object | Output given by the resource for others to refer.                                                                            | No         | ${config_map.RESOURCE_NAME.out}                        |
| out.OUT_NAME.OUT_NAME_NAME | string | This is the name of the output interface this can be any name depending on the number of prefix domains in the domains block | No         | ${config_map.RESOURCE_NAME.out.OUT_NAME.OUT_NAME_NAME} |

