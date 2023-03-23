# Introduction

[Mysql DB implementation using aws aurora](https://aws.amazon.com/rds/aurora/)

# Advanced Configuration

Use [link](https://registry.terraform.io/modules/terraform-aws-modules/rds/aws/latest#inputs) module variables within advanced section to configure other options. e.g. `username` input variable can be used to configure the username for master DB user.

# Considerations

- Mysql DB instance with aws aurora flavor can be configured with just `Writer` only. `Reader` is not mandatory