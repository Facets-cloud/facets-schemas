## Introduction

[Postgresql DB implementation using k8s flavor](https://github.com/bitnami/charts/tree/main/bitnami/postgresql)

## Spec


| Property           | Type                                                              | Required | Description                                              | 
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `postgres_version` | string                                                            | **Yes**  | Version of Postgres e.g. 12.11                           |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance | 

# Advanced Configuration
- use the helm chart [VALUES](https://artifacthub.io/packages/helm/bitnami/postgresql?modal=values) within the advanced section to override the default values.

```
    "advanced" : {
      "k8s": {
        "postgres": {
          "k8s_service_names": {
            
          },
          "values": {

          }
        }
      }
    }

```