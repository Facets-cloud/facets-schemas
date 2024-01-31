## Introduction
A service level intent which can be 
* Application
* Statefulset
* Cronjob
* Job

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
|--------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | [object](#advanced) | No       | Advanced section of kind service                                                                                                               |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)      | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |


## spec

Specification as per resource types schema

### Properties

| Property                    | Type               | Required | Description                                                                                                                                                             |
|-----------------------------|--------------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `enable_host_anti_affinity` | boolean            | **Yes**  | boolean to enable or disable host anti affinity                                                                                                                         |
| `release`                   | [object](#release) | **Yes**  | Map of all release keys                                                                                                                                                 |
| `runtime`                   | [object](#runtime) | **Yes**  | Map of all runtime keys                                                                                                                                                 |
| `type`                      | string             | **Yes**  | This will specify the type of service you want to create. Possible values are: `application`, `statefulset`, `cronjob`, `job`. |
| `env`                       | [object](#env)     | **Yes**       | The key value pairs of all the environment variables that needs to be passed to the pod                                                                                 |
| `restart_policy`            | string             | No       | Possible values are: `Always`, `OnFailure`, `Never`.                                                                         |

### release

Map of all release keys

| Property            | Type                         | Required | Description                                                          |
|---------------------|------------------------------|----------|----------------------------------------------------------------------|
| `strategy`          | [object](#strategy)          | **Yes**  |    The type of upgrade strategy to be followed by this service                                                                  |
| `build`             | [object](#build)             | No       | These contains the build objects required for running the containers |
| `disruption_policy` | [object](#disruption_policy) | No       | The disruption policy for the service                                                                     |
| `image`             | string                       | No       | The docker image of the application that you want to run             |

#### build

These contains the build objects required for running the containers

| Property      | Type   | Required | Description             |
|---------------|--------|----------|-------------------------|
| `artifactory` | string | **Yes**  | Parent artifactory name |
| `name`        | string | **Yes**  | Name of the artifactory |

#### disruption_policy
The disruption policy for the service 

| Property          | Type    | Required | Description                                                              |
|-------------------|---------|----------|--------------------------------------------------------------------------|
| `max_available`   | string | No       | This is the max number of pods should be available in case of failures   |
| `max_unavailable` | string | No       | This is the max number of pods that can be unavailable during a failure. |

#### strategy
The type of upgrade strategy to be followed by this service
##### Properties

| Property          | Type    | Required | Description                                                                                                  |
|-------------------|---------|----------|--------------------------------------------------------------------------------------------------------------|
| `max_available`   | string | No       | If type RollingUpdate , this is the max number of pods that can be created from the default replicas         |
| `max_unavailable` | string | No       | If type RollingUpdate , this is the max number of pods that can be unavailable from the default replicas     |
| `type`            | string  | **Yes**       | Your kubernetes rollout type , Possible values are: `RollingUpdate`, `Recreate`. |

### runtime

Map of all runtime keys

#### Properties

| Property        | Type                     | Required | Description                                                            |
|-----------------|--------------------------|----------|------------------------------------------------------------------------|
| `health_checks` | [object](#health_checks) | **Yes**  | All the health check related key value pairs                           |
| `ports`         | [object](#ports)         | **Yes**  | Maps of all the ports that you want to expose in the service           |
| `size`          | [object](#size)          | **Yes**  | Size of the deployment pods                                            |
| `args`          | array                    | No       | The list of arguments you want to pass for the above mentioned command |
| `autoscaling`   | [object](#autoscaling)   | No       |                                                                        |
| `command`       | array                    | No       | The list of commands you want to perform before starting the container |
| `metrics`       | [object](#metrics)       | No       | Maps of all the metrics port that you want to expose to prometheus     |
| `volumes`       | [object](#volumes)       | No       | All the volumes you want to attach to the service                      |

#### autoscaling

| Property              | Type                           | Required | Description                                                                                 |
|-----------------------|--------------------------------|----------|---------------------------------------------------------------------------------------------|
| `cpu_threshold`       | string                        | No       | The max cpu threshold that the hpa waits until it upscales                                  |
| `max`                 | string                        | No       | This is the max replicas where the hpa upscales to                                          |
| `min`                 | string                        | No       | This is the min replicas where the hpa downscales to                                        |
| `ram_threshold`       | string                        | No       | The max ram threshold that the hpa waits until it upscales                                  |


#### health_checks

All the health check related key value pairs

##### Properties

| Property                 | Type    | Required | Description                                                          |
|--------------------------|---------|----------|----------------------------------------------------------------------|
| `period`                 | string | **Yes**  | This is the repeated interval in which kubelet does a health check   |
| `port`                   | string | **Yes**  | Specify the port in which the health checks should be made           |
| `start_up_time`          | string | **Yes**  | The time kubernetes api needs to wait until the application is ready |
| `timeout`                | string | **Yes**  | Timeout for the health check                                         |
| `liveness_exec_command`  | array   | No       | The list of commands to make liveness check                          |
| `liveness_url`           | string  | No       | URL to make the liveness check                                       |
| `readiness_exec_command` | array   | No       | The list of commands to make readiness check                         |
| `readiness_url`          | string  | No       | URL to make the readiness check                                      |

#### metrics

Maps of all the metrics port that you want to expose to prometheus

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`^[a-zA-Z0-9_.-]*$`)  | No       | This is the name of the metrics, this can be any name

###### `^[a-zA-Z0-9_.-]*$`
This is the name of the metrics, this can be any name
| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `path`   | string | **Yes**       | The path where the service monitor should scrape metrics                                                                                     |
| `port_name`   | string | **Yes**        | Name of the service monitor                                                                                       |

#### ports

Maps of all the ports that you want to expose in the service
| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`^[a-zA-Z0-9_.-]*$`)  | No       | This is the name of the port that you have to specify

###### `^[a-zA-Z0-9_.-]*$`
This is the name of the port that you have to specify
| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `protocol`   | string | **Yes**       | The protocol of the port , 
| `type`            | string  | No       | Your kubernetes rollout type , Possible values are: `tcp`, `udp`                                                                                  |
| `port`   | string | **Yes**        | The port number where the pod is exposed                                                                                     |

#### size

Size of the deployment pods

| Property   | Type   | Required | Description                                                                                                                           |
|------------|--------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `cpu`      | string | No       | CPU request in format mentioned @ [link](https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu)       |
| `instance` | string | No       | Instance name in certain cases                                                                                                        |
| `memory`   | string | No       | Memory request in format mentioned @ [link](https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory) |
| `volume`   | string | No       | Volume request in kubernetes persistent volumes                                                                                       |

#### volumes

All the volumes you want to attach to the service

| Property  | Type               | Required | Description                                           |
|-----------|--------------------|----------|-------------------------------------------------------|
| `configs` | [object](#configs) | No       | The map of all the config maps that you want to mount |

#### configs

The map of all the config maps that you want to mount

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`^[a-zA-Z0-9_.-]*$`)  | No       | The map of all the config maps that you want to mount                                                                                       |
###### `^[a-zA-Z0-9_.-]*$`
The map of all the config maps that you want to mount
| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `mount_name`   | string | **Yes**       | The mount name for the config map                                                                                      |
| `mount_path`   | string | **Yes**        | The mount path for the config map                                                                                       |


## advanced

Advanced section of kind service

| Property | Type             | Required | Description                    |
|----------|------------------|----------|--------------------------------|
| `aws`    | [object](#aws)   | No       | Map of all aws advanced keys   |
| `chart`  | [object](#chart) | No       | Map of all chart advanced keys |
| `gcp`    | [object](#gcp)   | No       | Map of all gcp advanced keys   |

### aws

Map of all aws advanced keys

| Property | Type           | Required | Description                                                                   |
|----------|----------------|----------|-------------------------------------------------------------------------------|
| `iam`    | [object](#iam) | No       | This is the iam key where all iam permissions are attached for the deployment |

### chart

Map of all chart advanced keys

| Property             | Type                     | Required | Description                                                                              |
|----------------------|--------------------------|----------|------------------------------------------------------------------------------------------|
| `image_pull_secrets` | array                    | No       | list of all the image pull secrets for the docker image to be pulled from the repository |
| `node_selector`      | [object](#node_selector) | No       | Map of all node selectors for the application                                            |


### gcp

Map of all gcp advanced keys

| Property | Type           | Required | Description                                                                   |
|----------|----------------|----------|-------------------------------------------------------------------------------|
| `iam`    | [object](#iam) | No       | This is the iam key where all iam permissions are attached for the deployment |

## out

Output given by the resource for others to refer.

| Property     | Type                  | Required | Description                                                           |
|--------------|-----------------------|----------|-----------------------------------------------------------------------|
| `interfaces` | [object](#interfaces) | No       | The output for your service module, this can be generated or provided |

### Flavors

* `aws`
* `gcp`
* `azure`
