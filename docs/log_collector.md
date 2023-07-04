# Log Collector

## Schema Properties

|                       | Type                           | Description                                                                                                                                                                                                                              | Required   |
|:----------------------|:-------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent | string                         | Flag to enable the resource based on intent availability. eg mysql if mysql dashboard is required to be deployed. Note: Need to have the instance running beforehand to avail.                                                           | No         |
| depends_on            | array                          | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                                                                  | No         |
| disabled              | boolean                        | Flag to disable the resource                                                                                                                                                                                                             | No         |
| flavor                | enum: loki, loki_s3, loki_blob |                                                                                                                                                                                                                                          | Yes        |
| kind                  | enum: log_collector            |                                                                                                                                                                                                                                          | Yes        |
| lifecycle             | enum: ENVIRONMENT_BOOTSTRAP    | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                                                                                         | No         |
| provided              | boolean                        | Flag to tell if the resource should not be provisioned by facets                                                                                                                                                                         | No         |
| version               | enum: 0.1, latest              |                                                                                                                                                                                                                                          | Yes        |
| metadata.name         | string                         | Name of the resource                                                                                                                                                                                                                     | No         |
|                       |                                |     - if not specified, fallback is the `filename`                                                                                                                                                                                       |            |
| spec                  | object                         | Specification as per resource types schema                                                                                                                                                                                               | Yes        |
| spec.retentation_days | number                         | The number of days to retain an object after being created. Specify the number of days as an integer, e.g. 30 for 30 days                                                                                                                | Yes        |
| spec.storage_size     | string                         | Storage capacity to store logs. You can express storage size as a plain integer or as a fixed-point number using one of these quantity suffixes: E, P, T, G, M, k. You can also use the power-of-two equivalents: Ei, Pi, Ti, Gi, Mi, Ki | Yes        |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

