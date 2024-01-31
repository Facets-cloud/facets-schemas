## Introduction

A Postgres Instance in reader/writer datastore mode for alloydb postgres.

## Spec


| Property           | Type                                                              | Required | Description                                              | 
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `postgres_version` | string                                                            | **Yes**  | Version of Postgres e.g. 12.11                           |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |           


## Advanced Configuration

Use the following [link](https://registry.terraform.io/modules/GoogleCloudPlatform/alloy-db/google/1.1.2) to configure the advanced configuration for the Postgres Alloy DB module to invoke additional feature.
- All the advanced configuration can be configured that are mentioned in the above link and can be filled with desired parameter in the skeleton given below.
  ```
  "advanced": {
    "alloydb": {
      "alloy-db": {
      }
    }
  }
  ```


## Consideration

- To handle invocation of database flags as an advanced feature, refer the given json sinppet.
  ```
  "advanced": {
    "allloydb": {
      "alloy-db": {
        "primary_instance": {
          "database_flags": {
            "alloydb.logical_decoding" = "on"
          } 
        },
        "read_pool_instance": {
          "database_flags": {
            "alloydb.logical_decoding" = "on"
          } 
        }
      }
    }
  }
  ```