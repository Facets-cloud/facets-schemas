# Introduction

[Mysql DB implementation using azure](https://learn.microsoft.com/en-us/azure/mysql/flexible-server/overview)

# Advanced Configuration

Use the module [INPUT](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/mysql_flexible_server) variables within advanced section to configure other options. e.g. `standby_availability_zone`.

# Considerations

```
{
    "advanced": {
        "azure": {
          "flexible_server": {
            "example": "abc"
          }
        }
    }
}
```