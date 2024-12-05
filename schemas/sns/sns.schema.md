## Introduction
SNS is a web service that coordinates and manages the delivery or sending of messages to subscribing endpoints or clients.

## Spec

| Property                            | Type                    | Description                 |
|-------------------------------------|-------------------------|-----------------------------|
| subscription                        | [object](#Subscription) | Subscription for SNS topic. |


## Advanced

| Property | Type   | Description                                                                                                                                                                                             |
|----------|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| default  | Object | The advanced section of CloudFront module. You can pass the values as per terraform modules inputs - https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/sns_topic_subscription |

## Subscription

| Property                        | Type    | Description                                                                                                               |
|---------------------------------|---------|---------------------------------------------------------------------------------------------------------------------------|
| protocol                        | string  | Protocol to use.                                                                                                          |
| confirmation_timeout_in_minutes | integer | Integer indicating number of minutes to wait in retrying mode for fetching subscription arn before marking it as failure. |
| delivery_policy                 | string  | JSON String with the delivery policy                                                                                      |
| endpoint                        | string  | ARN of the Amazon SQS queue.                                                                                              |
| endpoint_auto_confirms          | boolean | Whether the endpoint is capable of auto confirming subscription                                                           |
| filter_policy                   | string  | JSON String with the filter policy that will be used in the subscription to filter messages seen by the target resource.  |
| filter_policy_scope             | string  | Enum: ["MessageAttributes", "MessageBody"]                                                                                |
| raw_message_delivery            | boolean | Whether to enable raw message delivery                                                                                    |
| redrive_policy                  | string  | JSON String with the redrive policy that will be used in the subscription                                                 |
| replay_policy                   | string  | JSON String with the archived message replay policy that will be used in the subscription.                                |

## Flavor
- default


## Output

| Name                | Type     | Description             |
|---------------------|----------|-------------------------|
| sns_queue_name      | `string` | Name of the SNS         |
| consumer_policy_arn | `string` | Policy ARN for consumer |
| consumer_policy_arn | `string` | Policy ARN for producer |
| topic_arn           | `string` | ARN of the SNS          |