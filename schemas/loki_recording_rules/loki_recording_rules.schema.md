## Introduction

The module generation creation of custom recording rules to be applied to respective intent module

## Spec

| Name     | Description                                                                                                                     | Datatype             | Required | Default |
|----------|---------------------------------------------------------------------------------------------------------------------------------|----------------------|----------|---------|
| `rules`  | Loki Recording rules allow you to run metric queries over your logs and derive a numeric aggregation from your logs, like calculating the number of requests over time from your NGINX access log then push it to prometheus server. | `Map<string, ?>` (required) | `Yes`    | `-`     |

## Rule

| Field Name      | Description                                                                                                                                                                                  | Datatype                         | Required |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------|----------|
| `expr`          | The recording rule expression. Uses LogQL language to query and process metrics data.                                                 | `string` (required)              | `Yes`    |
| `disabled` | Enable/Disable recording rule.                                                                                                                                                                 | `boolean`              | `No`    |
| `labels`        | The labels clause allows specifying a set of additional labels to be attached to the recording rule.                                             | `Map<string, string>` | `No`    |
