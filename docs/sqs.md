# Sqs

## Schema Properties

|                       | Type                        | Description                                                                                                                                                                    | Required   |
|:----------------------|:----------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent | string                      | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail. | No         |
| depends_on            | array                       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                        | No         |
| disabled              | boolean                     | Flag to disable the resource                                                                                                                                                   | No         |
| flavor                | enum: default               |                                                                                                                                                                                | Yes        |
| kind                  | enum: sqs                   |                                                                                                                                                                                | Yes        |
| lifecycle             | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                               | No         |
| provided              | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                               | No         |
| sqs_queue_arn         | string                      | ARN of the SQS                                                                                                                                                                 | No         |
| sqs_queue_name        | string                      | Name of the SQS                                                                                                                                                                | No         |
| sqs_queue_url         | string                      | URL of the SQS                                                                                                                                                                 | No         |
| version               | enum: 0.2                   |                                                                                                                                                                                | Yes        |
| metadata.name         | string                      | Name of the resource                                                                                                                                                           | No         |
|                       |                             |     - if not specified, fallback is the `filename`                                                                                                                             |            |
| spec                  | object                      |                                                                                                                                                                                | No         |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

