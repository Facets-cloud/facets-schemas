# Introduction

[Cache for Redis implementation using azure](https://learn.microsoft.com/en-us/azure/azure-cache-for-redis/cache-overview)

# Advanced Configuration

Use the module [INPUT](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs/resources/redis_cache) variables within advanced section to configure other options. e.g. `public_network_access_enabled`.

# Considerations

# Example
```json
{
    "advanced": {
        "azure_cache": {
            "public_network_access_enabled": "true"
    }
}
```
