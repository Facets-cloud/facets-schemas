## Introduction
SQS is a service where one service sends messages to the queue and another service receives those messages from the queue.

## Advanced

| Property                            | Type    | Required | Description                                                                                                |
|-------------------------------------|---------|----------|------------------------------------------------------------------------------------------------------------|
| `visibility_timeout_seconds`        | number  | **No**   | time taken by a consumer to process the message and keeps the message hidden from other consumers          |
| `message_retention_seconds`         | number  | **No**   | time until which message will be available in the queue if not deleted                                     |
| `max_message_size`                  | number  | **No**   | size of the message                                                                                        |
| `delay_seconds`                     | number  | **No**   | time that delivery of messages will be delayed                                                             |
| `receive_wait_time_seconds`         | number  | **No**   | The time for which a ReceiveMessage call will wait for a message to arrive (long polling) before returning |
| `policy`                            | string  | **No**   | policy for sqs                                                                                             |
| `redrive_policy`                    | string  | **No**   | defines how many times queue will make messages available for the consumers                                |
| `fifo_queue`                        | boolean | **No**   | ensures messages are delivered in correct order and at right time                                          |
| `content_based_deduplication`       | boolean | **No**   | if true ensures same messages will not be sent to the queue more than once                                 |
| `kms_master_key_id`                 | string  | **No**   | Id of master key that encrypts the sqs data                                                                |
| `kms_data_key_reuse_period_seconds` | number  | **No**   | time in which data key can be reused                                                                       |
| `tags`                              | object  | **No**   | tagging resources                                                                                          |

## Flavor
- default


## Output

| Name           | Type     | Description     |
|----------------|----------|-----------------|
| sqs_queue_name | `string` | Name of the SQS |
| sqs_queue_arn  | `string` | ARN of the SQS  |
| sqs_queue_url  | `string` | URL of the SQS  |