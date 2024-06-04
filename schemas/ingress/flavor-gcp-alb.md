# Introduction

GCP Application Loadbalancer Ingress implementation

## Advanced Configuration

Advanced configuraiton for gcp_alb

| Property                          | Type           | Required | Description                                                |
| --------------------------------- | -------------- | -------- | ---------------------------------------------------------- |
| `certificate_type`                | string         | No       | String to set the type of certificate (managed/k8s) in GCP |
| `dns`                             | [object](#dns) | No       | Add custom record type and record values under dns         |
| `enable_certificate_auto_renewal` | boolean        | No       | Flag to enable automatic certificate renewal               |

### dns

Advanced object to add custom record type and record values under dns

| Property       | Type   | Required | Description                                                  |
| -------------- | ------ | -------- | ------------------------------------------------------------ |
| `record_type`  | string | yes      | Name of the record type. For eg. CNAME, A, AAA               |
| `record_value` | string | yes      | Value of the record. Use comma separated for multiple values |

Example usage

```json
    {
      "advanced": {
        "gcp_alb": {
          "certificate_type": "managed",
          "dns": {
            "record_type": "CNAME",
            "record_value": "for.example.com"
          },
          "enable_certificate_auto_renewal": true
        }
      }
    }
```
