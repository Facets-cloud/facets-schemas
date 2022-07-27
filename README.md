# Intent Schema Guidelines

## General guidelines

- All multi-word keys should be snake case.
- Avoid arrays wherever possible
- All cloud tags/labels should be PascalCase
- If there is length restriction in names. Use given name if < desired length else use hashed name of desired length. ALso add the tag/ label for the resource


## Terms

| Term | Description |
|------|-------------|
|**Resource**| An entity declared in facets|
|**Type of Resource** or **Intent**|A resource expressible in facets JSON representation having a defined schema|
|**Implementation of Resource** or **Flavor**|A specific way of implementing a resource, for example redis can be implemented as a stateful set in kubernetes and elasticcache in AWS.|
|**Blueprint**|Collection of resource to create a functional product|
|**Environment**|Manifestation of this blueprint in any cloud|
|**User**|Developer or ops person who is creating the blueprint|

## Basic Schema for a resource type/ intent

- **kind**: Describes the type of resource
    - e.g. ingress, application, mysql etc.
    - if not specified, fallback is the `folder_name`/instances
- **flavor**: Implementation selector for the resource
    - e.g. for a resource type ingress, default, aws_alb, gcp_alb etc.
- **version**: In case of multiple versions of a resource implementation, this field can be used to pin to a particular
  version
    - default version is `latest`
- **lifecycle**: There are multiple phases in lifecycle of an environment launch, this field describes the phase in
  which the resource has to be invoked
    - `ENVIRONMENT_BOOTSTRAP`
    - `ENVIRONMENT_NORMAL` (default)
- **disabled**: Flag to disable the resource
    - boolean
- **provided**: Flag to tell if the resource should not be provisioned by facets
    - A MySQL can be provisioned outside the facets but can exist in the blueprint for other to refer the url, username
      etc.
    - the out _section_ has to be populated by the user
- **depends_on**: any dependencies on other resources
    - application x may depend on mysql y
    - `depends_on: ["mysql.y"]`
- **metadata**: Metadata related to the resource
    - name: Name of the resource
    - if not specified, _fallback is the `file name`_
- **spec**: Specification for the resource
    - Specification as per resource types schema
- **out**: Output given by the resource for others to refer.
    - Outputs as per resource types schema
- **advanced**: Additional fields if any for a particular implementation of a resource, to make it more configurable
  along with the standard fields

## Intent Schema

Each Intent or Resource type should have defined schema which should inherit from the base schema. These schemas should
be published in this git repository in `schema` folder for all to consume and refer. Along with the schema each resource
type should have a sample implementation in the same repository. Both of these should not be particular to any
implementation

Format: `intent.schema`

## Traits

There are many traits which may be common across resource types, like there can be multiple resources which expose their
endpoints or which take sizing, all such common features should be extracted in traits and everyone should inherit these
traits to avoid seamless experience to the user

e.g. Size trait

```json
{
  "size": {
    "cpu": 2,
    "memory": 4,
    "instance": "r4.xlarge"
  }
}
```

All traits should be documented and a sample file should be committed in the `traits` directory of this repository

## Out block

`Out` block is introduced so that the user may know what outputs are there in a resource type to refer in other
resources. This is very essential in wiring. For example an application may want to know other applications url or mysql
username etc.

Any field in this block can be referenced in other json by following convention

```json
{
  "format": "${resource_type.resource_name.out.fieldname}",
  "example": "${mysql.omsdb.out.interfaces.master.host}"
}
```

## Advanced Schema

Every implementation is free to inherit the resource type schema and add additional fields in `advanced` section. In
case of facets implementation these schemas can be added in the schema directory, external contributors can host this
schema anywhere. They will have to specify the schema while registering there module <Still to be implemented>

Format: `implementation.intent.schema`

# Intent Implementations Guidelines

Every module will have to contain a module.json file describing few aspects of that module.

Modules are broadly of two types:

- **Configurable**: These modules are invoked once and no user input is required to invoke them. However there can be an
  optional configuration file to configure the same -e.g. Prometheus, ecr_token_refresher
- **Instantiable**: These modules are invoked when there is a intent expressed by a user in his blueprint. This will be
  invoked once per json

## Module.json

- **provides**: Resource type or intent this module is implementing
- **flavors**: Keys for flavor for which this module will be invoked
- **supported_clouds**: This module is valid for which clouds
    - AWS, AZURE or GCP
- **version**: version of the implementation
- **depends_on**: any module this module depends on
- **lifecycle**: There are multiple phases in lifecycle of an environment launch, this field describes the phase in
  which the module has to be invoked
    - `ENVIRONMENT_BOOTSTRAP`
    - `ENVIRONMENT_NORMAL` (default)
- **input_type**: Configurable or instantiable
- **composition**: TBD
- **schema**: Schema location for this implementation if any

```json
{
  "provides": "kubernetes_node_pool",
  "flavors": [
    "default",
    "gke_node_pool"
  ],
  "supported_clouds": [
    "gcp"
  ],
  "version": "0.1",
  "depends_on": [
  ],
  "lifecycle": "ENVIRONMENT_BOOTSTRAP",
  "input_type": "instance",
  "composition": {
  }
}
```

## Location

Until the module registry is made modules are picked from the modules' directory of the infra code, they can be
organised in any folder structure

## Special Modules

Base Infra modules provisioning the Networking and Kubernetes infrastructure are special, and they do not follow thie
below working. They are invoked once as per the environments cloud.
`baseinfra_aws` `baseinfra_gcp` `baseinfra_azure`

## The Working

- A module is invoked only if it is applicable to that blueprint
- A module is invoked only if it supports the cloud where terraform is running. supported_cloud attribute is used to
  determine the same
- A module is invoked only if the declared flavor specified in intent json matches one of the ones in the module.
- Instantiable modules are named `module.resource_type_name` e.g. module.ingress_aws_alb
- There are some standard variables passed to the modules

```hcl
variable "cluster" {
  type        = any
  description = "Passed for legacy reasons, all details about the environment as configured by the user"
}

variable "baseinfra" {
  type        = any
  description = "VPC, Kubernetes, Subnet and SG details"
}

variable "cc_metadata" {
  type        = any
  description = "details of the control plane used"
}

variable "instance" {
  type        = any
  default     = {}
  description = "Intent/ Configuration Object"
}

variable "instance_name" {
  type        = string
  default     = ""
  description = "name of the resource as specified by the user"
}

variable "inputs" {
  type        = any
  default     = []
  description = "all outputs of the modules this one depends on"

}

variable "environment" {
  type        = any
  default     = {}
  description = "all environment related fields"

}
```

## Files

Following files are recommended while implementing a module

## General guidelines

- all multi-word directories and literals should be snake cased.
- **No providers should be specified in the implementation**
- try and reuse facets modules where ever possible to avoid code duplication

### output.tf

All outputs should be defined here as specified in the out schema of the intent/ resource type

each key will be output separately

```hcl
output "metadata" {
  value = helm_release.external_helm_charts.metadata
}
output "status" {
  value = helm_release.external_helm_charts.status
}
```

### versions.tf

all provider versions required by this module

### variables.tf

subset of the variables out of the ones required by this module. Please note that this file is replaced in runtime with
the standard variables so no default values are picked from here

### main.tf

Implementation of the module. More tf files can be created for code organization
