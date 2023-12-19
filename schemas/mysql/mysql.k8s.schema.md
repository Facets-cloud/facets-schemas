# Introduction

[Mysql DB implementation using k8s flavor](https://github.com/bitnami/charts/blob/main/bitnami/mysql)

# Advanced Configuration
- use the helm chart [VALUES](https://artifacthub.io/packages/helm/bitnami/mysql?modal=values) within the advanced section to override the default values. e.g: `image` can be changed in the following way

```
{
  "advanced": {
    "k8s": {
      "mysql": {
        "k8s_service_names": {},
        "values": {
          "image": {
            "tag": "latest"
          }
        }
      }
    }
  }
}
```

# Considerations

- Mysql DB instance with aws aurora flavor can be configured with just `Writer` only. `Reader` is not mandatory. In that case Reader block should be kept empty

```
"spec": {
      "mysql_version" : "8.0.34",
      "size" : {
        "writer": {
          "cpu": "1000m",
          "memory": "1000Mi",
          "volume": "8G"
        },
        "reader": {}
      }
    }
```