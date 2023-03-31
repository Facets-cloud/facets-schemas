## Introduction

A Postgres Instance in reader/writer datastore mode for cloudsql postgres.

## Spec


| Property           | Type                                                              | Required | Description                                              | 
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `postgres_version` | string                                                            | **Yes**  | Version of Postgres e.g. 12.11                           |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |           


## Advanced Configuration

Use the following [link](https://registry.terraform.io/modules/terraform-aws-modules/rds-aurora/aws/7.3.0#inputs) to configure the advanced configuration for the Postgres Aurora module to invoke additional feature.
- all the advanced configuration can be configured that are mentioned in the above link.

## Consideration

- Postgres Aurora database can be configured with only writer datastore.
- If the use case demands creation of aurora cluster with additional parameters then the given advanced block configuration skeleton can be used.
  
  ```
  "advanced": {
    "aurora": {
      "rds-aurora": {
      },
      "aws_db_parameter_group": {
      }
    }
  }
  ```


