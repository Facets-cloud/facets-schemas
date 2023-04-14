# Introduction

Grafana dashboard intent to create Grafana dashboards as ConfigMap in Kubernetes cluster

## Properties

| Property                | Type            | Required | Description                                                                                                                                    |
|-------------------------|-----------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`                | string          | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`                  | string          | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`              | object          | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`                  | [object](#spec) | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`               | string          | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`              | object          | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on`            |                 | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`              | boolean         | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`             | string          | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`                   | object          | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`              | boolean         | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |
| `conditional_on_intent` | string          | **Yes**  | Defining the resource dashboard is dependent on for implementation. e.g for resource of kind redis, value would be "redis"                     |

## spec

Specification as per resource types schema

### Properties

| Property    | Type   | Required | Description                                                                                                                                                                                                             |
| ----------- | ------ | -------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `dashboard` | object | No       | Grafana Dashboard json data. Please take a look at Dashboard JSON model here <https://grafana.com/docs/grafana/latest/dashboards/build-dashboards/view-dashboard-json-model/>. Note: `uid` attribute is auto generated. |
