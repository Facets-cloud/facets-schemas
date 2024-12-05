## Introduction

A Postgres Instance in replica/master datastore mode for rds postgres.

## Spec


| Property           | Type                                                              | Required | Description                                              | 
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `postgres_version` | string                                                            | **Yes**  | Version of Postgres e.g. 12.11                           |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |           


## Advanced Configuration

Use the following [link](Resource values as per the module documentation https://registry.terraform.io/modules/terraform-aws-modules/rds/aws/6.7.0#inputs) to configure the advanced configuration for the Postgres RDS module to invoke additional feature.
- all the advanced configuration can be configured that are mentioned in the above link.

## Consideration

- If the use case demands creation of postgres cluster with additional parameters then the given configuration skeleton should be used.
  
  ```
  "advanced": {
    "rds": {
      "rds-postgres": {
        "parameters": {
          "reader": {
            "authentication_timeout": {
              "value": 300,
              "apply_method": "pending-reboot"
            }
          },
          "writer": {
            "authentication_timeout": {
              "value": 600,
              "apply_method": "pending-reboot"
            }
          }
        }
      }
    }
  }
  ```


