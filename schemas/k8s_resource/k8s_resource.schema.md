## Properties

| Property   | Type                | Required | Description                               |
|------------|---------------------|----------|-------------------------------------------|
| `flavor`   | string              | **Yes**  | Possible values are: `default`, `k8s`.    |
| `kind`     | string              | **Yes**  | Possible values are: `k8s_resource`.      |
| `spec`     | [object](#spec)     | **Yes**  |                                           |
| `version`  | string              | **Yes**  | Possible values are: `0.1`, `0.2`, `0.3`. |
| `advanced` | [object](#advanced) | No       | Advanced section for the module           |
| `out`      | [object](#out)      | No       |                                           |

## advanced

Advanced section for the module

### Properties

| Property       | Type                    | Required | Description                                 |
|----------------|-------------------------|----------|---------------------------------------------|
| `k8s_resource` | [object](#k8s_resource) | No       | Advanced parameters for k8s_resource module |

### k8s_resource

Advanced parameters for k8s_resource module

#### Properties

| Property          | Type    | Required | Description                                                           |
|-------------------|---------|----------|-----------------------------------------------------------------------|
| `cleanup_on_fail` | boolean | No       | Whether to clean up when the resource installation fails              |
| `timeout`         | integer | No       | Timeout for the resource                                              |
| `wait`            | boolean | No       | Whether to wait until all the resources has been successfully created |

## out

### Properties

| Property     | Type                  | Required | Description |
|--------------|-----------------------|----------|-------------|
| `attributes` | [object](#attributes) | No       |             |

### attributes

#### Properties

| Property        | Type   | Required | Description                  |
|-----------------|--------|----------|------------------------------|
| `resource_name` | string | No       | The name of the k8s resource |

## spec

### Properties

| Property               | Type                            | Required | Description                                                                                   |
|------------------------|---------------------------------|----------|-----------------------------------------------------------------------------------------------|
| `additional_resources` | [object](#additional_resources) | **Yes**  | This should contain a map of kubernetes YAML manifest converted to JSON and pasted in a block |
| `resource`             | [object](#resource)             | No       | This should contain the kubernetes YAML manifest converted to JSON and pasted in a block      |

### additional_resources

This should contain a map of kubernetes YAML manifest converted to JSON and pasted in a block

| Property | Type | Required | Description |
|----------|------|----------|-------------|

### resource

This should contain the kubernetes YAML manifest converted to JSON and pasted in a block

#### Properties

| Property     | Type   | Required | Description |
|--------------|--------|----------|-------------|
| `apiVersion` | string | No       |             |
| `kind`       | string | No       |             |
| `metadata`   |        | No       |             |

