# Introduction

Nginx Ingress Controller implementation

## Advanced Configuration

Advanced configuraiton for nginx_ingress_controller

| Property                        | Type    | Required | Description                                                                                                                                                                                                                         |
| ------------------------------- | ------- | -------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `acm`                           | string  | No       | The ARN of the ACM certificate created for that domain                                                                                                                                                                              |
| `connection_idle_timeout`       | string  | No       | The load balancer has a configured idle timeout period (in seconds) that applies to its connections. If no data has been sent or received by the time that the idle timeout period elapses, the load balancer closes the connection |
| `enable-underscores-in-headers` | boolean | No       | Enables underscores in header names                                                                                                                                                                                                 |

Example usage

```json
    {
      "advanced": {
        "nginx_ingress_controller": {
          "enable-underscores-in-headers": true
        }
      }
    }
  ```
