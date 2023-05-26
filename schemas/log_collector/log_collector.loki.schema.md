# Introduction

Log collector of flavor loki_s3 implementation

## Advanced Configuration
 
The following variables are related to the installation and configuration of various components of the Loki distributed log system using Helm charts:

| Name | Description | Datatype | Required |
| --- | --- | --- | --- |
| loki.loki.values | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-distributed<br>Note: By default loki.structuredConfig.storage_config is configured to use minio storage backend | map&lt; string, any &gt; | no |
| loki.minio.values | Helm values as per the chart https://artifacthub.io/packages/helm/bitnami/minio | map&lt; string, any &gt; | no |
| loki.promtail.values | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/promtail<br>Note: By default, loki push endpoint will be configured automatically to send log entries to Loki | map&lt; string, any &gt; | no |
| loki.loki_canary.values | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-canary<br>Note: By default, lokiAddress will be set with loki endpoint and serviceMonitor is enabled | map&lt; string, any &gt; | no |
| loki.loki_canary.enable_loki_canary | Whether to enable loki canary or not | boolean | no |


## Example usage
```json 
    "advanced": {
        "loki": {
            "loki": {
                "values": {}
            },
        "minio": {},
        "promtail": {},
        "loki_canary": {}
        }
    }
    
```