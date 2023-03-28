# Introduction

[Mysql DB implementation using aws aurora](https://aws.amazon.com/rds/aurora/)

# Advanced Configuration

- Use the module [INPUT](https://registry.terraform.io/modules/terraform-aws-modules/rds/aws/latest#inputs) variables within advanced section to configure other options. e.g. `username` input variable can be used to configure the username for master DB user.
- Additional parameters can be added through advanced section within `rds-aurora` block
```
{
  "advanced": {
    "aurora": {
      "rds-aurora": {}
    }
  }
}
```


# Considerations

- Mysql DB instance with aws aurora flavor can be configured with just `Writer` only. `Reader` is not mandatory
- Users can add more tags through metadata for e.g.
```
{
  "metadata": {
    "tags": {
      "TAG1": "test-tag1",
      "TAG2": "test-tag2"
    }
  }
}
```