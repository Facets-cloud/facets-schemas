# Introduction

GCP Application Loadbalancer Ingress implementation

## Advanced Configuration

Advanced configuraiton for gcp_alb

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `enable_managed_certificates`   | boolean | No       | Boolean flag to enable or disable the creation of managed certificates in GCP

Example usage

```json
    {
      "advanced": {
        "gcp_alb": {
          "enable_managed_certificates": false
        }
      }
    }
  ```
