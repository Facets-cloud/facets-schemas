# Introduction

[Mysql DB implementation using gcp cloudsql](https://cloud.google.com/sql/)

# Advanced Configuration

Use the module [INPUT](https://github.com/terraform-google-modules/terraform-google-sql-db/tree/v13.0.1/modules/mysql) variables within advanced section to configure other options. e.g. `database_flags` input variable can be used to configure list of cloudsql flags to be applied tp DB server.

# Considerations

- For cloudsql `disk_autoresize` is set to true by default so disk storage will be expanded automatically. 
- Users can add more tags/labels through advanced section e.g.
```
{
  "advanced": {
    "user_labels": {
      "db": "mysql"
    }
  }
}
```