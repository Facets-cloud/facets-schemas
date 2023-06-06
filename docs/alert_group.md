# Alert Group

## Schema Properties

|                                | Type                        | Description                                                                                                                                                                                   | Required   |
|:-------------------------------|:----------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------|
| conditional_on_intent          | string                      |                                                                                                                                                                                               | No         |
| depends_on                     | array                       | Dependencies on other resources. e.g. application x may depend on mysql                                                                                                                       | No         |
| disabled                       | boolean                     | Flag to disable the resource                                                                                                                                                                  | No         |
| flavor                         | enum: default               |                                                                                                                                                                                               | Yes        |
| kind                           | enum: alert_group           |                                                                                                                                                                                               | Yes        |
| lifecycle                      | enum: ENVIRONMENT_BOOTSTRAP | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`)                                                                                              | No         |
| provided                       | boolean                     | Flag to tell if the resource should not be provisioned by facets                                                                                                                              | No         |
| version                        | enum: 0.1, latest           |                                                                                                                                                                                               | Yes        |
| metadata.name                  | string                      | Name of the resource                                                                                                                                                                          | No         |
|                                |                             |     - if not specified, fallback is the `filename`                                                                                                                                            |            |
| spec                           | object                      | Specification as per resource types schema                                                                                                                                                    | Yes        |
| spec.rules.KEY.annotations.KEY | string                      | Annotation value                                                                                                                                                                              | No         |
| spec.rules.KEY.expr            | string                      | alert expression                                                                                                                                                                              | No         |
| spec.rules.KEY.for             | string                      | The optional `for` clause causes Prometheus to wait for a certain duration between first encountering a new expression output vector element and counting an alert as firing for this element | No         |
| spec.rules.KEY.labels.KEY      | string                      | Label value                                                                                                                                                                                   | No         |
| spec.rules.KEY.message         | string                      | The message for the alert rule                                                                                                                                                                | No         |
| spec.rules.KEY.resource_name   | string                      | The resource name. Eg: {{ $labels.pod }}                                                                                                                                                      | No         |
| spec.rules.KEY.resource_type   | string                      | The resource type. Eg: pod                                                                                                                                                                    | No         |
| spec.rules.KEY.severity        | string                      | The alert rule severity                                                                                                                                                                       | No         |
| spec.rules.KEY.summary         | string                      | The summary of the alert rule                                                                                                                                                                 | No         |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

