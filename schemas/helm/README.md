## Introduction

This intent lets you install any helm chart in your facets environment. 

## Properties

| Attribute              | Type                                | Description                                                                                  |
|------------------------|-------------------------------------|----------------------------------------------------------------------------------------------|
| `kind`                 | `string` (const)                    | Kind of the intent, always set to 'helm'                                                     |
| `flavor`               | `string` (const)                    | Flavor for the Helm chart implementation, always set to 'default'                            |
| `spec`                 | `object`                            | Specification of the Helm chart intent                                                       |
| `spec.helm.chart`      | `string`                            | URL or local path to the Helm chart package                                                  |
| `spec.helm.namespace`  | `string`                            | Namespace to deploy the Helm chart in                                                        |
| `spec.helm.wait`       | `boolean`                           | Whether to wait for the Helm chart deployment to complete                                    |
| `spec.env`             | `array` of `objects`                | List of dynamic values to be passed to the chart, equivalent to --set parameters in helm cli |
| `spec.env[].name`      | `string`                            | Name of the property to be passed to the helm chart                                          |
| `spec.env[].type`      | `string` (enum: `static`, `secret`) | Type of dynamic variable                                                                     |
| `spec.env[].attribute` | `string`                            | Name of the secret as defined in stack.json (only applicable for 'secret' type)              |
| `spec.env[].default`   | `string`                            | Default value for the helm variable (only applicable for 'static' type)                      |
| `spec.values`          | `object`                            | The values to be passed on to the chart in the form of a json object                         |
