## Introduction
A tcp loadbalancer level intent which can be used to create a network loadbalancer which can be exposed to the internet / an internal loadblancer in a specific cloud for tcp / udp based resources in the backend

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
|--------------|---------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `flavor`     | string              | **Yes**  | Implementation selector for the resource. e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.                                     |
| `kind`       | string              | **Yes**  | Describes the type of resource. e.g. ingress, application, mysql etc. If not specified, fallback is the `folder_name`/instances                |
| `metadata`   | [object](#metadata) | **Yes**  | Metadata related to the resource                                                                                                               |
| `spec`       | [object](#spec)     | **Yes**  | Specification as per resource types schema                                                                                                     |
| `version`    | string              | **Yes**  | This field can be used to pin to a particular version                                                                                          |
| `advanced`   | [object](#advanced) | No       | Additional fields if any for a particular implementation of a resource                                                                         |
| `depends_on` |                     | No       | Dependencies on other resources. e.g. application x may depend on mysql                                                                        |
| `disabled`   | boolean             | No       | Flag to disable the resource                                                                                                                   |
| `lifecycle`  | string              | No       | This field describes the phase in which the resource has to be invoked (`ENVIRONMENT_BOOTSTRAP`) Possible values are: `ENVIRONMENT_BOOTSTRAP`. |
| `out`        | [object](#out)      | No       | Output given by the resource for others to refer.                                                                                              |
| `provided`   | boolean             | No       | Flag to tell if the resource should not be provisioned by facets                                                                               |

## spec

Specification as per resource types schema

| Property     | Type                | Required | Description                                                                                                                                                                                                                                            |
|--------------|---------------------|----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `instances`  | integer             | **Yes**        | Number of instances that you want to create per_instance loadbalancers                                                                                                                                                                                 |
| `mode`       | string              | **Yes**        | Mode can be either loadbalancing or per_instance. loadbalancing is for a single instance loadbalancing for backend applications and per_instance is for targeting a specific backend application Possible values are: `loadbalancing`, `per_instance`. |
| `pod_prefix` | string              | **Yes**        | The pod prefix to which the loadbalancer routes requests to                                                                                                                                                                                            |
| `ports`      | [object](#ports)    | **Yes**        | The map of all the ports that you need to expose on the loadbalancer                                                                                                                                                                                   |
| `private`    | boolean             | **Yes**        | Make this load balancer private                                                                                                                                                                                                                        |
| `selector`   | [object](#selector) | **Yes**        | Map of all the kubernetes selectors that are required to map the loadbalancer service to applications in the backend                                                                                                                                   |

### ports

The map of all the ports that you need to expose on the loadbalancer

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `^[a-zA-Z0-9_.-]*$`   | [object](#`^[a-zA-Z0-9_.-]*$`)  | **Yes**       | The map of all the ports that you need to expose on the loadbalancer                                                                                       |
###### `^[a-zA-Z0-9_.-]*$`
The map of all the ports that you need to expose on the loadbalancer
| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `protocol`   | string | **Yes**       | the protocol of the port , Possible values: `tcp` or `udp`                                                                                    |
| `port`   | string | **Yes**        | Port number where the service needs to be accessible via the loadbalancer its the same port that will be exposed via the loadbalancer                                                                                    |

## advanced

Additional fields if any for a particular implementation of a resource

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `nlb`   | [object](#nlb)  | No       | Advanced section of the nlb

## nlb

Advanced section of the nlb
| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `service_annotations`   | object | No       | Extra annotations that you want to add to the kubernetes service annotations block

## out

Output given by the resource for others to refer.

| Property               | Type  | Required | Description                                                          |
|------------------------|-------|----------|----------------------------------------------------------------------|
| `loadbalancer_domains` | array | No       | This is the list of loadbalancer domains that the module has created |

### Flavors

* `nlb`
