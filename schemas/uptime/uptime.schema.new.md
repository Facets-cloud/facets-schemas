## Properties

| Property   | Type                | Required | Description                                                                                                                                                    |
|------------|---------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`   | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc. Possible values are: `default`.                     |
| `kind`     | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances Possible values are: `uptime`. |
| `spec`     | [object](#spec)     | **Yes**  |                                                                                                                                                                |
| `version`  | string              | **Yes**  | This field can be used to pin to a particular version Possible values are: `0.1`, `latest`.                                                                    |
| `advanced` | [object](#advanced) | No       | Advanced uptime Schema                                                                                                                                         |

## advanced

Advanced uptime Schema

### Properties

| Property | Type              | Required | Description                       |
|----------|-------------------|----------|-----------------------------------|
| `uptime` | [object](#uptime) | No       | Advanced values for uptime module |

### uptime

Advanced values for uptime module

#### Properties

| Property            | Type            | Required | Description                                                                                         |
|---------------------|-----------------|----------|-----------------------------------------------------------------------------------------------------|
| `image_pull_policy` | string          | No       | Imagepull policy for the uptime check check Possible values are: `Always`, `IfNotPresent`, `Never`. |
| `image`             | string          | No       | Image for the uptime check                                                                          |
| `run_interval`      | string          | No       | How often to run the check                                                                          |
| `size`              | [object](#size) | No       |                                                                                                     |
| `timeout`           | string          | No       | Timeout for uptime check                                                                            |

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
| `http`   | [object](#http)  | No       | HTTP configuration for the uptime service.  |
| `mongo`  | [object](#mongo) | No       | Mongo configuration for the uptime service. |
| `redis`  | [object](#redis) | No       | Redis configuration for the uptime service. |
| `tcp`    | [object](#tcp)   | No       | TCP configuration for the uptime service.   |

### http

HTTP configuration for the uptime service.

#### Properties

| Property               | Type   | Required | Description                                                           |
|------------------------|--------|----------|-----------------------------------------------------------------------|
| `expected_status_code` | string | **Yes**  | The expected status code that you want as response.                   |
| `method`               | string | **Yes**  | The HTTP method. Possible values are: `GET`, `POST`, `PUT`, `DELETE`. |
| `url`                  | string | **Yes**  | The URL of the service.                                               |
| `body`                 | string | No       | The Body that needs to be passed.                                     |
| `count`                | string | No       | The number of times to check for the uptime.                          |
| `expected_response`    | string | No       | The expected response.                                                |
| `headers`              | string | No       | The HTTP headers that needs to passed.                                |

### mongo

Mongo configuration for the uptime service.

#### Properties

| Property | Type   | Required | Description                                       |
|----------|--------|----------|---------------------------------------------------|
| `url`    | string | **Yes**  | The URL / Connection string of the mongo service. |

### redis

Redis configuration for the uptime service.

#### Properties

| Property | Type   | Required | Description                                       |
|----------|--------|----------|---------------------------------------------------|
| `url`    | string | **Yes**  | The URL / Connection string of the redis service. |

### tcp

TCP configuration for the uptime service.

#### Properties

| Property | Type   | Required | Description                 |
|----------|--------|----------|-----------------------------|
| `url`    | string | **Yes**  | The URL of the tcp service. |

