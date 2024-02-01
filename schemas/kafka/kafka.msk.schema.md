# Introduction

[Kafka implementation using aws msk]()

# Advanced Configuration

- use the module [INPUT](https://registry.terraform.io/modules/terraform-aws-modules/msk-kafka-cluster/aws/1.2.0?tab=inputs) variables within advanced section to configure other options.


```
{
  "advanced": {
    "kafka": {
      "msk": {
      }
    }
  }
}
```
# Values
## Advanced Configuration for Kafka MSK

This document outlines the advanced configuration options for Kafka MSK, which can be passed as JSON when configuring the MSK cluster.

| Name                                   | Data Type | Default Value             | Description                                                                                                          |
|----------------------------------------|-----------|---------------------------|----------------------------------------------------------------------------------------------------------------------|
| cloudwatch_config                      | Object    | NA                        | Configuration for CloudWatch Logs integration.                                                                       |
| cloudwatch_logs_enabled                | Boolean   | true                      | Enable or disable CloudWatch Logs integration for Kafka broker logs.                                                 |
| cloudwatch_log_group_name              | String    | null                      | Name of the CloudWatch Logs log group to which Kafka broker logs will be sent.                                       |
| cloudwatch_log_group_kms_key_id        | String    | null                      | KMS Key ID for encrypting Kafka broker logs in the CloudWatch log group.                                             |
| cloudwatch_log_group_retention_in_days | Integer   | null                      | Number of days to retain Kafka broker logs in the CloudWatch log group.                                              |
| configuration_name                     | String    | configuration             | Name of the Kafka MSK configuration.                                                                                 |
| configuration_description              | String    | configuration description | Description of the Kafka MSK configuration.                                                                          |
| configuration_server_properties        | Object    | NA                        | Kafka server properties for the configuration.                                                                       |
| jmx_exporter_enabled                   | Boolean   | false                     | Enable or disable JMX Exporter for monitoring Kafka metrics.                                                         |
| node_exporter_enabled                  | Boolean   | false                     | Enable or disable Node Exporter for monitoring node-level metrics.                                                   |
| enhanced_monitoring                    | String    | DEFAULT                   | Level of enhanced monitoring for Kafka brokers (DEFAULT, PER_BROKER, PER_TOPIC_PER_BROKER, PER_TOPIC_PER_PARTITION). |
| client_authentication_sasl_iam         | Boolean   | true                      | Enable or disable IAM-based client authentication using SASL.                                                        |
| client_authentication_sasl_scram       | Boolean   | false                     | Enable or disable SCRAM-based client authentication using SASL.                                                      |
| create_scram_secret_association        | Boolean   | false                     | Allow MSK to create associations between Kafka brokers and SCRAM secrets.                                            |
| scaling_role_arn                       | String    | null                      | IAM role ARN for cluster scaling operations.                                                                         |
| scaling_max_capacity                   | Integer   | 250                       | Maximum number of broker nodes when scaling the cluster.                                                             |
| scaling_target_value                   | Integer   | 80                        | Target value for triggering scaling based on broker CPU utilization.                                                 |
| tags                                   | Object    | Server_Properties         | Tags to associate metadata with the Kafka MSK cluster.                                                               |


### Configuration
#### Cloudwatch configuration
```json
{
  "cloudwatch_config": {
    "cloudwatch_logs_enabled": true,
    "cloudwatch_log_group_name": null,
    "cloudwatch_log_group_kms_key_id": null,
    "cloudwatch_log_group_retention_in_days": 5
  }
}
```

#### Server_Properties
```json
{
  "configuration_server_properties" : {
    "key-01" : "value-01",
    "key-02" : "value-02"
  }
}
```
Please refer to the above table for detailed information about the advanced configuration options for Kafka MSK. These options can be customized by passing a JSON configuration when setting up a Kafka MSK cluster.

# Considerations

##### NA