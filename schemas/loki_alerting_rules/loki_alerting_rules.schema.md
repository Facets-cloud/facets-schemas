## Introduction

The module generation creation of custom alerts to be applied to respective intent module

## Spec

| Name     | Description                                                                                                                     | Datatype             | Required | Default |
|----------|---------------------------------------------------------------------------------------------------------------------------------|----------------------|----------|---------|
| `rules`  | Loki Alerting rules allow you to define alert conditions based on LogQL expression language expressions and to send notifications about firing alerts to an external service. | `Map<string, ?>` (required) | `Yes`    | `-`     |

## Rule

| Field Name      | Description                                                                                                                                                                                  | Datatype                         | Required |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------|----------|
| `expr`          | The alert expression that defines the condition triggering the alert when evaluated. Uses LogQL language to query and process metrics data.                                                 | `string` (required)              | `Yes`    |
| `for`           | The optional for clause causes Loki to wait for a certain duration between first encountering a new expression output vector element and counting an alert as firing for this element. | `string` (required)              | `Yes`    |
| `summary`       | The summary of the alert rule that provides a short description of the alert rule and is included in the notification message.                                                               | `string` (required)              | `Yes`    |
| `message`       | The detailed information about the alert that is included in the notification message.                                                                                                       | `string` (required)              | `Yes`    |
| `severity`      | The severity indicates the level of importance or urgency of the alert.                                                                                                                      | `string` (required)              | `Yes`    |
| `resource_name` | The resource name, e.g. {{ $labels.pod }}.                                                                                                                                                   | `string` (required)              | `Yes`    |
| `resource_type` | The resource type, e.g. pod.                                                                                                                                                                 | `string` (required)              | `Yes`    |
| `labels`        | The labels clause allows specifying a set of additional labels to be attached to the alert. Any existing conflicting labels will be overwritten.                                             | `Map<string, string>` (required) | `Yes`    |
| `annotations`   | The annotations clause specifies a set of informational labels that can be used to store longer additional information such as alert descriptions or runbook links.                          | `Map<string, string>` (required) | `Yes`    |
| `disabled` | Enable/Disable alert rule.                                                                                                                                                                 | `boolean`              | `No`    |
