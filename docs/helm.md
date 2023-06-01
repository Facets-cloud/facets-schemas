# Helm

## Schema Properties

|                     | Type              | Description                                                                          | Required   |
|:--------------------|:------------------|:-------------------------------------------------------------------------------------|:-----------|
| kind                | const: helm       | Kind of the intent, always set to 'helm'                                             | Yes        |
| flavor              | const: default    | Flavor for the Helm chart implementation, always set to 'default'                    | Yes        |
| spec.helm.chart     | string            | URL or local path to the Helm chart package                                          | No         |
| spec.helm.namespace | string            | Namespace to deploy the Helm chart in                                                | No         |
| spec.helm.wait      | boolean           | Whether to wait for the Helm chart deployment to complete                            | No         |
| spec.env            | array             | List of dynamic values to be passed to the chart, i.e to reference a variable/secret | No         |
| spec.values         | object            | The values to be passed on to the chart in the form of a json object                 | No         |
| version             | enum: 0.1, latest |                                                                                      | Yes        |
| disabled            | boolean           | Flag to disable the resource                                                         | No         |
| provided            | boolean           | Flag to tell if the resource should not be provisioned by facets                     | No         |
| depends_on          | array             | Dependencies on other resources. e.g. application x may depend on y                  | No         |
| metadata.name       | string            | Name of the resource                                                                 | No         |
|                     |                   |     - if not specified, fallback is the `filename`                                   |            |

## Outputs

| Type   | Description   | Required   | Referencing   |
|--------|---------------|------------|---------------|

