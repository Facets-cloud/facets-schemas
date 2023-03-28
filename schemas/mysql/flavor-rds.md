# Introduction

[Mysql DB implementation using aws rds](https://aws.amazon.com/rds/)

# Advanced Configuration

- use the module [INPUT](https://github.com/terraform-aws-modules/terraform-aws-rds#inputs) variables within advanced section to configure other options. e.g. `username` input variable can be used to configure the username for master DB user.
- Additional parameters can be configured with in the rds-mysql block of advanced section e.g.

```
{
  "advanced": {
    "rds": {
      "rds-mysql": {
      }
    }
  }
}
```

# Considerations

- Mysql DB instance with aws rds flavor can be configured with just `Writer` only. `Reader` is not mandatory