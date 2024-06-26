# Introduction

AWS Application Loadbalancer Ingress implementation

## Advanced Configuration

Advanced configuraiton for aws_alb

| Property                    | Type           | Required | Description                                                                                                                                                         |
|-----------------------------|----------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `waf_arn`                   | string         | No       | ARN of WAF that you can hook into ALB                                                                                                                               |
| `waf_acl_id`                | string         | No       | ACL id of the WAF                                                                                                                                                   |
| `shield`                    | string         | No       | Enable/Disable shield in AWS                                                                                                                                        |
| `target_type`               | string         | No       | backend target type of service in kubernetes Possible values : `ip` and `instance` : default is `ip`                                                                |
| `disable_facets_host_rules` | boolean        | No       | Boolean flag to enable or disable facets host rules in alb, in order to incorporate more rules. Since there is a hard limit of 100 rules in alb: default is `false` |
| `maintain_rule_order`       | boolean        | No       | Boolean flag to maintain the order of rules specified in json using priority numbers inside rules                                                                   |
| `dns`                       | [object](#dns) | No       | Add custom record type and record values under dns                                                                                                                  |

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
        "aws_alb": {
          "disable_facets_host_rules": false,
          "dns": {
            "record_type": "CNAME",
            "record_value": "for.example.com"
          }
        }
      }
    }
  ```
