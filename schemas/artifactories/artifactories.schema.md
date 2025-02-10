# Artifactories (Container Registry) JSON Schema

This JSON Schema defines for Artifactories (Container Registry) resources.

## Properties

| Property  | Type            | Required | Description                                                           |
|-----------|-----------------|----------|-----------------------------------------------------------------------|
| `flavor`  | string          | **Yes**  | Possible values are: `default`.                                       |
| `kind`    | string          | **Yes**  | Possible values are: `artifactories`.                                 |
| `spec`    | [object](#spec) | **Yes**  |                                                                       |
| `version` | string          | **Yes**  | Possible values are: `0.1`, `latest`.                                 |
| `out`     | [object](#out)  | No       | The output for your artifactories module, this can be generated or provided |

## out

The output for your artifactories module, this can be generated or provided

### Properties

| Property     | Type                  | Required | Description                                            |
|--------------|-----------------------|----------|--------------------------------------------------------|
| `attributes` | [object](#attributes) | No       | The output attributes section for artifactories module |

### attributes

The output attributes section for artifactories module

#### Properties

| Property                  | Type                               | Required | Description                                                                                         |
|---------------------------|------------------------------------|----------|-----------------------------------------------------------------------------------------------------|
| `registry_secret_objects` | [object](#registry_secret_objects) | No       | Map of Container Registry secrets. Eg: {<container_registry_name>: [{"name": "<k8s_secret_name>"}]} |
| `registry_secrets_list`   | array                              | No       | List of Container Registry secrets                                                                  |

#### registry_secret_objects

Map of Container Registry secrets. Eg: {<container_registry_name>: [{"name": "<k8s_secret_name>"}]}

| Property | Type | Required | Description |
|----------|------|----------|-------------|

## spec

### Properties

| Property        | Type                     | Required | Description                                                                                                                                  |
|-----------------|--------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------|
| `artifactories` | [object](#artifactories) | No       | A mapping of container registries. Example: {<registry1>: "<container_registry_name>", <registry2>: "<container_registry_name>"}             |
| `include_all`   | boolean                  | No       | Include all container registries mapped to this project. Note: The `artifactories` attribute must not be specified if this attribute is used |

### artifactories

A mapping of container registries. Example: {<registry1>: "<container_registry_name>", <registry2>: "<container_registry_name>"}

| Property | Type   | Required |       Description       |
|----------|--------|----------|-------------------------|
| name     | string | **Yes**  | Container Registry Name |
