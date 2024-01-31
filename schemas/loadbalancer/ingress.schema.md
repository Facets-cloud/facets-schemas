## Introduction
A loadbalancer level intent which can be used to create an application or network loadbalancer which can be exposed to the internet / an internal loadblancer in a specific cloud

## Properties

| Property     | Type                | Required | Description                                                                                                                                    |
| ------------ | ------------------- | -------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
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

## Spec

Specification as per resource types schema

| Property                | Type               | Required | Description                                                                              |
| ----------------------- | ------------------ | -------- | ---------------------------------------------------------------------------------------- |
| `force_ssl_redirection` | boolean            | **Yes**  | Force SSL redirection from http to https                                                 |
| `private`               | boolean            | **Yes**  | Make this load balancer private                                                          |
| `rules`                 | [object](#rules)   | **Yes**  | Objects of all ingress rules                                                             |
| `basicAuth`             | boolean            | No       | Enable or disable basic auth                                                             |
| `domains`               | [object](#domains) | No       | The map of domain key to rules                                                           |
| `grpc`                  | boolean            | No       | Enable or Disable grpc support in nginx_ingress_controller                               |
| `ipv6_enabled`          | boolean            | No       | Enable/disable ipv6, this is a cloud specific check if your vpc has ipv6 support enabled |

### domains

The map of domain key to rules

| Property            | Type                      | Required | Description                                                                        |
|---------------------|---------------------------|----------|------------------------------------------------------------------------------------|
| `^[a-zA-Z0-9_.-]*$` | [object](#`domain_regex`) | No       | This is the name of the domain object that you are creating, it can be of any name |


#### `domian_regex`

| Property                | Type   | Required | Description                                                                                     |
|-------------------------|--------|----------|-------------------------------------------------------------------------------------------------|
| `domain`                | string | **Yes**  | The host name of the domain                                                                     |
| `alias`                 | string | **Yes**  | Alias naming for the domain                                                                     |
| `certificate_reference` | string | **Yes**  | Certificate reference name. For `flavor` `aws_alb`, it's the ACM ARN certificate reference. For `gcp_alb`, it's the name of the managed certificate in GCP. For `nginx_ingress_controller`, it's the uploaded Kubernetes certificate referenced as a secret. For `azure_agic`, it is the secret key ID of the secret in Azure Key Vault. |

### rules

Objects of all ingress rules

| Property            | Type                     | Required | Description                                                                         |
|---------------------|--------------------------|----------|-------------------------------------------------------------------------------------|
| `^[a-zA-Z0-9_.-]*$` | [object](#`rules_regex`) | **Yes**  | This is the name of the ingress object that you are creating, it can be of any name |

###### `rules_regex`
| Property        | Type    | Required | Description                                                                                         |
|-----------------|---------|----------|-----------------------------------------------------------------------------------------------------|
| `service_name`  | string  | **Yes**  | The Kubernetes service name of the application                                                      |
| `path`          | string  | **Yes**  | Path of the application                                                                             |
| `port_name`     | string  | **Yes**  | Port name of the service                                                                            |
| `port`          | integer | **Yes**  | Port number of the service                                                                          |
| `domain_prefix` | string  | **Yes**  | Subdomain prefix for the service                                                                     |
| `priority`      | string  | **Yes**  | Priority number for the above rule (ranging from 1 to 1000) and it should be unique for each rule. This property is applicable only for `aws_alb` version `0.2`. |


## Out

Output given by the resource for others to refer.

### Properties

| Property     | Type                  | Required | Description                                                           |
| ------------ | --------------------- | -------- | --------------------------------------------------------------------- |
| `interfaces` | [object](#interfaces) | No       | The output for your ingress module, this can be generated or provided |

## Flavors

- [aws_alb](flavor-aws-alb.md)
- [gcp_alb](flavor-gcp-alb.md)
- [nginx_ingress_controller](flavor-nginx.md)
- [azure_agic](flavor-azure-agic.md)

## Alerts

| Alert Name         | Description                                                    |
| ------------------ | -------------------------------------------------------------- |
| `IngressOutOfSync` | Invalid ingress configuration will block load balancer updates |
