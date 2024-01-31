## Properties

| Property   | Type                | Required | Description                                                                                                                                                    |
|------------|---------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`   | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc. Possible values are: `default`.                     |
| `kind`     | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances Possible values are: `status_check`. |
| `spec`     | [object](#spec)     | **Yes**  |                                                                                                                                                                |
| `version`  | string              | **Yes**  | This field can be used to pin to a particular version Possible values are: `0.1`, `latest`.                                                                    |
| `advanced` | [object](#advanced) | No       | Advanced status_check Schema                                                                                                                                         |

## advanced

Advanced status_check Schema

#### Properties

| Property | Type              | Required | Description                       |
|----------|-------------------|----------|-----------------------------------|
| `status_check` | [object](#status_check) | No       | Advanced values for status_check module |

### status_check

Advanced values for status_check module

#### Properties

| Property            | Type            | Required | Description                                                                                         |
|---------------------|-----------------|----------|-----------------------------------------------------------------------------------------------------|
| `image_pull_policy` | string          | No       | Imagepull policy for the status_check check check Possible values are: `Always`, `IfNotPresent`, `Never`. |
| `image`             | string          | No       | Image for the status_check check                                                                          |
| `run_interval`      | string          | No       | How often to run the check                                                                          |
| `size`              | [object](#size) | No       |                                                                                                     |
| `timeout`           | string          | No       | Timeout for status_check check                                                                            |

#### size

##### Properties

| Property       | Type   | Required | Description                                                                                                   |
|----------------|--------|----------|---------------------------------------------------------------------------------------------------------------|
| `cpu`          | string | **Yes**  | The number of CPU cores required, e.g '1' or '1000m'                                                          |
| `memory`       | string | **Yes**  | The amount of memory required, e.g '800Mi' or '1.5Gi'                                                         |
| `cpu_limit`    | string | No       | The CPU limit to set a maximum limit on the amount of CPU resources utilization, e.g '1' or '1000m'           |
| `memory_limit` | string | No       | The memory limit to set a maximum limit on the amount of memory resources utilization, e.g '800Mi' or '1.5Gi' |

## spec

### Properties

| Property | Type             | Required | Description                                 |
|----------|------------------|----------|---------------------------------------------|
| `http`   | [object](#http)  | No       | HTTP configuration for the status_check service.  |
| `mongo`  | [object](#mongo) | No       | Mongo configuration for the status_check service. |
| `redis`  | [object](#redis) | No       | Redis configuration for the status_check service. |
| `tcp`    | [object](#tcp)   | No       | TCP configuration for the status_check service.   |



### http

HTTP configuration for the status_check service.

#### Properties

Maps of all the http checks want to expose to create 

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`httpcheck`)  | No       | This is the name of the http check, this can be any name

#### `^[a-zA-Z0-9_.-]*$`

| Property               | Type   | Required | Description                                                           |
|------------------------|--------|----------|-----------------------------------------------------------------------|
| `expected_status_code` | string | **Yes**  | The expected status code that you want as response.                   |
| `method`               | string | **Yes**  | The HTTP method. Possible values are: `GET`, `POST`, `PUT`, `DELETE`. |
| `url`                  | string | **Yes**  | The URL of the service.                                               |
| `body`                 | string | No       | The Body that needs to be passed.                                     |
| `count`                | string | No       | The number of times to check for the status_check.                          |
| `expected_response`    | string | No       | The expected response.                                                |
| `headers`              | string | No       | The HTTP headers that needs to passed.                                |

### mongo

Mongo configuration for the status_check service.

#### Properties

Maps of all the mongo checks want to expose to create 

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`mongocheck`)  | No       | This is the name of the mongo check, this can be any name

#### `^[a-zA-Z0-9_.-]*$`

| Property | Type   | Required | Description                                       |
|----------|--------|----------|---------------------------------------------------|
| `url`    | string | **Yes**  | The URL / Connection string of the mongo service. |

### redis

Redis configuration for the status_check service.

#### Properties

Maps of all the redis checks want to expose to create 

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`redischeck`)  | No       | This is the name of the redis check, this can be any name

#### `^[a-zA-Z0-9_.-]*$`

| Property | Type   | Required | Description                                       |
|----------|--------|----------|---------------------------------------------------|
| `url`    | string | **Yes**  | The URL / Connection string of the redis service. |

### tcp

TCP configuration for the status_check service.

#### Properties

Maps of all the redis checks want to expose to create 

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`tcpcheck`)  | No       | This is the name of the tcp check, this can be any name

#### tcpcheck

| Property | Type   | Required | Description                 |
|----------|--------|----------|-----------------------------|
| `url`    | string | **Yes**  | The URL of the tcp service. |

