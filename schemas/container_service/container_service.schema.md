## Introduction
Schema for the AWS ECS Cluster


## Spec


| Property  | Type                | Required | Description                                |
|-----------|---------------------|----------|--------------------------------------------|
| `cluster` | [Cluster](#Cluster) | **Yes**  | Cluster level settings for AWS ECS cluster |


## Cluster
| Property    | Type   | Required | Description                                   |
|-------------|--------|----------|-----------------------------------------------|
| `lifecycle` | string | **Yes**  | Lifecycle for the Fargate capacity providers. |


## out

| Property     | Type                      | Required | Description                      |
|--------------|---------------------------|----------|----------------------------------|
| `attributes` | [Attributes](#Attributes) | **Yes**  | Output Attributes (# Attributes) |

## Attributes
| Property       | Type   | Required | Description             |
|----------------|--------|----------|-------------------------|
| `cluster_name` | string | **Yes**  | Name of the ECS Cluster |
| `cluster_arn`  | string | **Yes**  | ARN of the ECS Cluster  |


## Flavors

- ecs_cluster