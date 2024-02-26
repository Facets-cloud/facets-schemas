# Introduction

GCP Application Loadbalancer Ingress implementation

## Advanced Configuration

Advanced configuraiton for gcp_alb

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `certificate_type`   | string | No       | String to set the type of certificate (managed/k8s) in GCP

Example usage

```json
    {
      "advanced": {
        "gcp_alb": {
          "certificate_type": "managed"
        }
      }
    }
  ```
