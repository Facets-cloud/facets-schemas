## Introduction

A Postgres Instance in reader/writer datastore mode for cloudsql postgres.

## Spec


| Property           | Type                                                              | Required | Description                                              | 
|--------------------|-------------------------------------------------------------------|----------|----------------------------------------------------------|
| `postgres_version` | string                                                            | **Yes**  | Version of Postgres e.g. 12.11                           |
| `size`             | [object](../../traits/reader-writer-datastore-sizing.schema.json) | **Yes**  | Sizing attribute for postgres writer and reader instance |           


## Advanced Configuration

Use the following [link](https://github.com/terraform-google-modules/terraform-google-sql-db/tree/v13.0.1/modules/postgresql) to configure the advanced configuration for the Postgres Cloudsql module to invoke additional feature.
- All the advanced configuration can be configured that are mentioned in the above link and can be filled with desired parameter in the skeleton given below.
  ```
  "advanced": {
    "cloudsql": {
      "postgres": {
      }
    }
  }
  ```


## Consideration 

- Postgres Cloudsql database cannot be configured with only writer datastore as it's equivalent reader datastore is mandatory for creation with minimal sizing.
- If encountered with instance deletion/destroy failure. Kindly refer to the given [runbook](https://share-docs.clickup.com/3443930/d/h/3936u-18744/caa022affb5455a) for resolution.
- If the use case demands creation of reader replication across different zone for HA then refer to the given snippet to invoke module's advanced feature. 
  Here, kindly specify the given readers along with it's desired zone. 
  ```
  "advanced": {
    "cloudsql": {
      "postgres": {
        "reader_zone" : {
          "reader-0" : "asia-south1-b",
          "reader-1" : "asia-south1-c"
        }
      }
    }
  }
  ```
- To handle invocation of database flags as an advanced feature, refer the given json sinppet. **Only replace `flag*` with your desired flag name.** 
  ```
  "advanced": {
    "cloudsql": {
      "postgres": {
        "database_flags": {
          "flag1": {
            "name" : "pg_stat_statements.save",
            "value" : "true"
          },
          "flag2": {
            "name" : "pg_stat_statements.max",
            "value" : "300"
          }
        }
      }
    }
  }
  ```
- To invoke advanced features like additional_database & additional_user, refer to this nomenclature to enable.Refer to `additional_users` & `additional_databases` respectively. 
  ```
  "advanced": {
    "cloudsql": {
      "postgres": {
        "additional_users": {
          "user1": {
            "name": "tester-1",
            "password": "postgres"
          },
          "user2": {
            "name": "tester-2",
            "password": "postgres"
          }
        },
        "additional_databases": {
          "db1": {
            "name": "additional-test-db"
          },
          "db2": {
            "name": "additional-test-db-2"
          }
        }
      }
    }
  }
  ```
