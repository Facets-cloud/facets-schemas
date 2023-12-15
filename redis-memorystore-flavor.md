---
title: "Redis memorystore flavor"
slug: "redis-memorystore-flavor"
hidden: true
createdAt: "2022-10-12T05:59:45.083Z"
updatedAt: "2022-10-12T06:05:06.843Z"
---
This is the implementation to create an instance of memorystore database for GCP.

Terraform resources used:

- [google_redis_instance](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/redis_instance)

## JSON values

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
    "0-3": "`memorystore`",
    "1-0": "`kind`",
    "1-1": "No",
    "1-2": "string",
    "1-3": "`redis`",
    "2-0": "`version`",
    "2-1": "No",
    "2-2": "string",
    "2-3": "`0.1`  \nDefault value - `latest`.",
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
    "8-3": "See [out](https://readme.facets.cloud/docs/redis-intent#out)."
  },
  "cols": 4,
  "rows": 9,
  "align": [
    "left",
    "left",
    "left",
    "left"
  ]
}
[/block]

## Advanced Schema

[Redis Memorystore flavor schema ](https://github.com/Facets-cloud/facets-schemas/blob/master/schemas/redis/flavor-memorystore.schema.json)

### Advanced values

| Key                                 | Optional | Type | Description                                                                                                                                                   |
| :---------------------------------- | :------- | :--- | :------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `memorystore.google_redis_instance` | Yes      | Map  | Resource values as per the [terraform resource documentation](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/redis_instance). |