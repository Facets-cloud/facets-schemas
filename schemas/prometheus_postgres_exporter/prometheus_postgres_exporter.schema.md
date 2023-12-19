# Introduction
Configuration that intends to configure prometheus postgres exporter in the Facets environment.

## Properties

| Property                | Type                | Required | Description                                                                                                                          |
|-------------------------|---------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------|
| `kind`                  | string              | **Yes**  | Describes the type of resource. Possible values are: `configuration`.                                                                |
| `for`                   | string              | **Yes**  | Possible values are: `prometheus_postgres_exporter`.                                                                                 |
| `metadata`              | [object](#metadata) | **Yes**  | Metadata related to the resource.                                                                                                    |
| `version`               | string              | **Yes**  | This field can be used to pin to a particular version. Possible values are: `0.1`, `latest`.                                         |
| `advanced`              | [object](#advanced) | No       | Advanced Prometheus Postgres Exporter Schema.                                                                                                          |
| `depends_on`            |                     | No       | Dependencies on other resources. e.g., application x may depend on MySQL.                                                            |
| `disabled`              | boolean             | No       | Flag to disable the resource.                                                                                                        |
| `lifecycle`             | string              | No       | Describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`). Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `conditional_on_intent` | string              | No       | Defining the resource dashboard is dependent on for implementation. e.g., for a resource of kind redis, value would be "redis".      |

## Advanced

Advanced Prometheus Postgres Exporter Schema

### Properties

| Property                              | Type   | Required | Description                                                                                                                             |
|---------------------------------------|--------|----------|-----------------------------------------------------------------------------------------------------------------------------------------|
| `prometheus_postgres_exporter.values` | object | No       | Helm values for [prometheus-postgres-exporter](https://artifacthub.io/packages/helm/prometheus-community/prometheus-postgres-exporter). |
