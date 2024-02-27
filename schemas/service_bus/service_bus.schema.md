## Introduction
This document describes the structure of the Azure Service Bus configuration schema.

### spec 

The `spec` object defines the configuration for Azure Service Bus.

| Property | Type              | Description           |
|----------|-------------------|-----------------------|
| `topics` | [object](#topics) | Topics configuration. |
| `queues` | [object](#queues) | Queues configuration. |
| `size`   | [object](#size)   | Size configuration.   |

#### topics

The `topics` object defines the configuration for topics in Azure Service Bus.

| Property                        | Type    | Description                                                                                                  | Default    | Required |
|---------------------------------|---------|--------------------------------------------------------------------------------------------------------------|------------|----------|
| `name`                          | string  | The name of the topic.                                                                                       | <key_name> | No       |
| `status`                        | string  | The status of the topic.                                                                                     | "Active"   | No       |
| `enable_batched_operations`     | boolean | (Optional) Indicates whether batched operations are enabled for the topic.                                   | `true`     | No       |
| `enable_express`                | boolean | (Optional) Indicates whether express (small message) option is enabled for the topic.                        | `false`    | No       |
| `enable_partitioning`           | boolean | (Optional) Indicates whether partitioning is enabled for the topic. Default is `false` for non-Premium SKUs. | `false`    | No       |
| `max_message_size_in_kilobytes` | number  | (Optional) The maximum message size in kilobytes for the topic. Default is `null` for Premium SKUs.          | -          | No       |
| `max_size_in_megabytes`         | number  | (Optional) The maximum size of the topic in megabytes.                                                       | -          | No       |
| `requires_duplicate_detection`  | boolean | (Optional) Indicates whether duplicate detection is enabled for the topic.                                   | `false`    | No       |
| `support_ordering`              | boolean | (Optional) Indicates whether message ordering is supported for the topic.                                    | `false`    | No       |

#### queues

The `queues` object defines the configuration for queues in Azure Service Bus.


| Property                               | Type    | Description                                                                                                                    | Default  | Required |
|----------------------------------------|---------|--------------------------------------------------------------------------------------------------------------------------------|----------|----------|
| `name`                                 | string  | The name of the queue.                                                                                                         | <key>    | No       |
| `status`                               | string  | The status of the queue.                                                                                                       | "Active" | No       |
| `lock_duration`                        | string  | (Optional) The duration that the message is locked when being processed. Default is "PT1M" (1 minute).                         | `null`   | No       |
| `max_message_size_in_kilobytes`        | number  | (Optional) The maximum message size in kilobytes for the queue. Default is `null`.                                             | `null`   | No       |
| `max_size_in_megabytes`                | number  | (Optional) The maximum size of the queue in megabytes. Default is `null`.                                                      | `null`   | No       |
| `requires_duplicate_detection`         | boolean | (Optional) Indicates whether duplicate detection is enabled for the queue. Default is `false`.                                 | `false`  | No       |
| `requires_session`                     | boolean | (Optional) Indicates whether the queue requires session support. Default is `false`.                                           | `false`  | No       |
| `dead_lettering_on_message_expiration` | boolean | (Optional) Indicates whether to dead-letter messages when they expire. Default is `false`.                                     | `false`  | No       |
| `max_delivery_count`                   | number  | (Optional) The maximum delivery count for a message before it's dead-lettered. Default is 10.                                  | 10       | No       |
| `enable_batched_operations`            | boolean | (Optional) Indicates whether batched operations are enabled for the queue. Default is `true`.                                  | `true`   | No       |
| `enable_partitioning`                  | boolean | (Optional) Indicates whether partitioning is enabled for the queue. Default is `false` for non-Premium SKUs.                   | `false`  | No       |
| `enable_express`                       | boolean | (Optional) Indicates whether express (small message) option is enabled for the queue. Default is `false` for non-Premium SKUs. | `false`  | No       |
| `forward_to`                           | string  | (Optional) The name of the entity to forward messages to. Default is `null`.                                                   | `null`   | No       |
| `forward_dead_lettered_messages_to`    | string  | (Optional) The name of the entity to forward dead-lettered messages to. Default is `null`.                                     | `null`   | No       |




#### size

The `size` object defines the size configuration for Azure Service Bus.

| Property    | Type   | Description                         |
| ----------- | ------ | ----------------------------------- |
| `sku`       | string | SKU (Stock Keeping Unit) of Azure Service Bus. |
| `capacity`  | string | The capacity of Azure Service Bus. |

**Note**: The `sku` property is required in the `size` configuration.


## Flavors

- NA

## Outputs

| Property     | Type                                                                 | Required | Description |
|--------------|----------------------------------------------------------------------|----------|-------------|
| `interfaces` | [primary](../../traits/multi-node-datastore-interface.schema.json)   | No       |             |
| `interfaces` | [secondary](../../traits/multi-node-datastore-interface.schema.json) | No       |             |

