# Introduction

Nginx Ingress Controller implementation

## Advanced Configuration

Advanced configuraiton for nginx_ingress_controller

| Property                        | Type           | Required | Description                                                                                                                                                                                                                         |
|---------------------------------|----------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `acm`                           | string         | No       | The ARN of the ACM certificate created for that domain                                                                                                                                                                              |
| `connection_idle_timeout`       | string         | No       | The load balancer has a configured idle timeout period (in seconds) that applies to its connections. If no data has been sent or received by the time that the idle timeout period elapses, the load balancer closes the connection |
| `enable-underscores-in-headers` | boolean        | No       | Enables underscores in header names                                                                                                                                                                                                 |
| `dns`                           | [object](#dns) | No       | Add custom record type and record values under dns                                                                                                                                                                                  |

### dns

Advanced object to add custom record type and record values under dns

| Property       | Type   | Required | Description                                                  |
|----------------|--------|----------|--------------------------------------------------------------|
| `record_type`  | string | yes      | Name of the record type. For eg. CNAME, A, AAA               |
| `record_value` | string | yes      | Value of the record. Use comma separated for multiple values |

Example usage

```json
    {
      "advanced": {
        "nginx_ingress_controller": {
          "enable-underscores-in-headers": true,
          "dns": {
            "record_type": "CNAME",
            "record_value": "for.example.com"
          }
        }
      }
    }
  ```
