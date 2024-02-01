# Introduction

Log collector of flavor loki_gcs implementation

## Advanced Configuration

## Advanced

| Name                                | Description                                                                                                                                                                                  | Datatype           | Required | Supported Version  |
| ----------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------ | -------- | -------- |
| bucket_name                         | GCS bucket name to distribute chunks. <br><br> Note: You can use google_cloud_storage module to create bucket and map the name using `${google_cloud_storage.<name-of-your-resource>.name}`                        | string             | yes      |   0.2 |
| loki_gcs.loki.values                    | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-distributed  Note: By default loki.structuredConfig.storage_config is configured to use minio storage backend | map< string, any > | no       |  0.2         |
| loki_gcs.minio.values                   | Helm values as per the chart https://artifacthub.io/packages/helm/bitnami/minio                                                                                                              | map< string, any > | no       |   0.2        |
| loki_gcs.promtail.values                | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/promtail  Note: By default, loki push endpoint will be configured automatically to send log entries to Loki        | map< string, any > | no       |  0.2         |
| loki_gcs.loki_canary.values             | Helm values as per the chart https://artifacthub.io/packages/helm/grafana/loki-canary  Note: By default, lokiAddress will be set with loki endpoint and serviceMonitor is enabled            | map< string, any > | no       |  0.2         |
| loki_gcs.loki_canary.enable_loki_canary | Whether to enable loki canary or not                                                                                                                                                         | boolean            | no       |  0.2        |
| loki_gcs.loki.ingester_pvc_size         | Expand the volume size of ingester PVC                                                                                                                                                           | string             | no       |  0.2        |
| loki_gcs.loki.querier_pvc_size          | Expand the volume size of querier PVC                                                                                                                                                            | string             | no       |  0.2        |

## Example usage
```json
   "advanced": {
        "loki_gcs": {
            "bucket_name": "loki",
            "loki": {
                "values": {}
            },
            "minio": {
                "values": {}
            },
            "promtail": {
                "values": {}
            },
        "loki_canary": {
            "enable_loki_canary": true,
            "values": {}
            }
        }
  }
```
