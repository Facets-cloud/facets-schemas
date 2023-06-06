# Schemahero Database

## Schema Properties

|                       | Type                        | Description                                                                                                                                                                    | Required   |
|:----------------------|:----------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent | string                      | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on            | array                       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled              | boolean                     | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                | enum: default               | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                                                     | Yes        |
| kind                  | string                      | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                                                | Yes        |
| lifecycle             | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided              | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| version               | enum: 0.1                   | This field can be used to pin to a particular version                                                                                                                          | Yes        |
| metadata.name         | string                      | Name of the resource                                                                                                                                                           | No         |
|                       |                             |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                      | Spec block of the resource                                                                                                                                                     | Yes        |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

