---
title: "Redis"
slug: "redis-intent"
hidden: true
createdAt: "2022-10-07T05:07:15.037Z"
updatedAt: "2022-10-12T06:48:08.008Z"
---
Redis `intent `can be provisioned in the following flavors:

- [K8s](https://readme.facets.cloud/docs/redis-k8s-flavor)
- [Elasticache](https://readme.facets.cloud/docs/redis-elasticache-flavor)
- [Memorystore](https://readme.facets.cloud/docs/redis-memorystore-flavor)

# Base JSON values

[block:parameters]
{
  "data": {
    "h-0": "Key",
    "h-1": "Optional",
    "h-2": "Type",
    "h-3": "Values",
    "0-0": "`flavor`",
    "0-1": "No",
    "0-2": "string",
    "0-3": "`k8s`, `elasticache`, `memorystore`",
    "1-0": "`kind`",
    "1-1": "No",
    "1-2": "string",
    "1-3": "`redis`",
    "2-0": "`version`",
    "2-1": "No",
    "2-2": "string",
    "2-3": "Any eligible version.  \nDefault value - `latest`.",
    "3-0": "`disabled`",
    "3-1": "Yes",
    "3-2": "boolean",
    "3-3": "`true`, `false`  \nDefault value - `true`",
    "4-0": "`provided`",
    "4-1": "Yes",
    "4-2": "boolean",
    "4-3": "`true`, `false`  \nDefault value - `false`",
    "5-0": "`depends_on`",
    "5-1": "Yes",
    "5-2": "array",
    "5-3": "Default value - `[]`",
    "6-0": "`metadata`",
    "6-1": "No",
    "6-2": "object",
    "6-3": "See [metadata](https://readme.facets.cloud/docs/redis-intent#metadata).",
    "7-0": "`spec`",
    "7-1": "No",
    "7-2": "object",
    "7-3": "See [spec](https://readme.facets.cloud/docs/redis-intent#spec).",
    "8-0": "`out`",
    "8-1": "Yes",
    "8-2": "object",
    "8-3": "See [out](https://readme.facets.cloud/docs/redis-intent#out).",
    "9-0": "`advanced`",
    "9-1": "Yes",
    "9-2": "object",
    "9-3": "See [advanced](https://readme.facets.cloud/docs/redis-intent#advanced)."
  },
  "cols": 4,
  "rows": 10,
  "align": [
    "left",
    "left",
    "left",
    "left"
  ]
}
[/block]

## metadata

| Key    | Optional | Type   | Description                                                          |
| :----- | :------- | :----- | :------------------------------------------------------------------- |
| `name` | Yes      | string | Name of the resource. If not specified, fall back is the `filename`. |

## spec

| Key                   | Optional | Type                                                                    | Description                                |
| :-------------------- | :------- | :---------------------------------------------------------------------- | :----------------------------------------- |
| `authenticated`       | No       | boolean                                                                 | Flag to make this redis password protected |
| `redis_version`       | No       | string                                                                  | Version of redis e.g. 6.3                  |
| `persistence_enabled` | No       | boolean                                                                 | Flag to enable persistence for this redis. |
| `size`                | No       | [Reader-Writer](https://readme.facets.cloud/docs/modules#reader-writer) | Size of Reader-Writer.                     |

### Example spec JSON

```json
  "spec": {
    "authenticated": true,
    "redis_version": "6.2",
    "persistence_enabled": true,
    "size": {
      "writer": {
        "cpu": "1",
        "memory": "512M",
        "replica_count": 1
      },
      "reader": {
        "cpu": "1",
        "memory": "512M",
        "replica_count": 0
      }
    }
  }
```



## out

> ❗ NOTE
> 
> The out trait references all properties of the spec trait.

| Key          | Optional | Type                                                                                        | Description                   |
| :----------- | :------- | :------------------------------------------------------------------------------------------ | :---------------------------- |
| `spec`       | Yes      | [Spec](https://readme.facets.cloud/docs/redis-intent#spec)                                  | Complete Spec of this module. |
| `interfaces` | Yes      | [Reader-Writer Interface](https://readme.facets.cloud/docs/modules#reader-writer-interface) | Master SD details.            |
| `instances`  | Yes      | Map\<string, [Interface](https://readme.facets.cloud/docs/modules#interface)>               | List of all instances.        |