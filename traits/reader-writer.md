## Introduction

Placeholder

## Properties

| Property | Type              | Required | Description        |
|----------|-------------------|----------|--------------------|
| `writer` | [object](#writer) | **Yes**  | The writer details |
| `reader` | [object](#reader) | No       | The reader details |

## reader

The reader details

### Properties

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of reader instances needs to be deployed                                                                                       |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

## writer

The writer details

### Properties

| Property        | Type    | Required | Description                                                                                                                           |
|-----------------|---------|----------|---------------------------------------------------------------------------------------------------------------------------------------|
| `replica_count` | integer | **Yes**  | Number of writer instances needs to be deployed                                                                                       |
| `cpu`           | string  | No       | CPU request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-cpu       |
| `instance`      | string  | No       | Instance name in certain cases                                                                                                        |
| `memory`        | string  | No       | Memory request in format mentioned @ https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/#meaning-of-memory |
| `volume`        | string  | No       | Volume request in kubernetes persistent volumes                                                                                       |

