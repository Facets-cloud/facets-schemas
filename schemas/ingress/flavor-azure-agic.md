# Introduction

Azure Application Gateway Ingress Controller implementation

## Advanced Configuration

Advanced configuraiton for azure_agic

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `values`   | string | No       | Helm Values as per the [helm chart](https://github.com/Azure/application-gateway-kubernetes-ingress/tree/master/helm/ingress-azure) 

Example usage

```json
    {
      "advanced": {
        "azure_agic": {
          "values": {
            "kubernetes": {
              "resources": {
                "limits": {
                  "cpu": "1000m"
                }
              }
            }
          }
        }
      }
    }
  ```
