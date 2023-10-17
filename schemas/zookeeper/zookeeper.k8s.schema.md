# Introduction

[Apache zookeeper for k8s](https://github.com/bitnami/charts/tree/main/bitnami/zookeeper)

# Advanced Configuration

Use the values [INPUT](https://github.com/bitnami/charts/blob/main/bitnami/zookeeper/values.yaml) variables within advanced section to configure other options. e.g. `replicaCount`.

# Considerations

# Example
```json
{
    "advanced" : {
      "k8s": {
        "zookeeper": {
          "values": {
            "replicaCount": 1
          }
        }
      }
    }
}
```
