{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "capillary-base.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "capillary-base.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "capillary-base.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create environment variables from SD auth secrets for CRM Apps
*/}}
{{- define "crmConfigurations" -}}
{{- $crmModuleName := .Values.crmModuleName}}
{{- range .Values.crmConfigurations }}
          - name: {{ . }}_USERNAME
            valueFrom:
              secretKeyRef:
                name: {{ . | lower | replace "_" "-"}}
                key: {{ $crmModuleName | upper }}_USERNAME
          - name: {{ . }}_PASSWORD
            valueFrom:
              secretKeyRef:
                name: {{ . | lower | replace "_" "-"}}
                key: {{ $crmModuleName | upper }}_PASSWORD
{{- end }}
{{- end -}}

{{/*
Create standard configs volumes for CRM Apps
*/}}
{{- define "crmConfigVolumes" }}
      volumes:
      - configMap:
          defaultMode: 420
          name: sd-configs
        name: sd-configs
      - configMap:
          defaultMode: 420
          name: redis-properties
        name: redis-properties
      - configMap:
          defaultMode: 420
          name: comm-engine-redis-properties
        name: comm-engine-redis-properties
      - configMap:
          defaultMode: 420
          name: region-conf
        name: region-conf
      - name: sd-private-key
        secret:
          defaultMode: 420
          secretName: sd-private-key
      - name: sd-public-key
        secret:
          defaultMode: 420
          secretName: sd-public-key
      - name: intouch-sharding-policy
        secret:
          defaultMode: 420
          items:
          - key: intouch-sharding-policy.json
            path: intouch-sharding-policy.json
          secretName: intouch-sharding-policy
      - name: conquest-sharding-policy
        secret:
          defaultMode: 420
          items:
          - key: conquest-sharding-policy.json
            path: conquest-sharding-policy.json
          secretName: conquest-sharding-policy
      - name: recommender-sharding-policy
        secret:
          defaultMode: 420
          items:
          - key: recommender-sharding-policy.json
            path: recommender-sharding-policy.json
          secretName: recommender-sharding-policy
      - name: timeline-sharding-policy
        secret:
          defaultMode: 420
          items:
          - key: timeline-sharding-policy.json
            path: timeline-sharding-policy.json
          secretName: timeline-sharding-policy
{{- end -}}


{{/*
Mount standard config volumes for CRM Apps
*/}}
{{- define "crmConfigVolumesMounts" }}
          volumeMounts:
          - mountPath: /etc/capillary/service-discovery/sd.config.properties
            name: sd-configs
            subPath: sd.config.properties
          - mountPath: /etc/capillary/redis.properties
            name: redis-properties
            subPath: redis.properties
          - mountPath: /etc/capillary/comm-engine-redis.properties
            name: comm-engine-redis-properties
            subPath: comm-engine-redis.properties
          - mountPath: /etc/region.conf
            name: region-conf
            subPath: region.conf
          - mountPath: /etc/capillary/service-discovery/sd.private.key
            name: sd-private-key
            subPath: sd.private.key
          - mountPath: /etc/capillary/service-discovery/sd.public.key
            name: sd-public-key
            subPath: sd.public.key
          - mountPath: /etc/capillary/service-discovery/intouch-sharding-policy
            name: intouch-sharding-policy
          - mountPath: /etc/capillary/service-discovery/conquest-sharding-policy
            name: conquest-sharding-policy
          - mountPath: /etc/capillary/service-discovery/recommender-sharding-policy
            name: recommender-sharding-policy
          - mountPath: /etc/capillary/service-discovery/timeline-sharding-policy
            name: timeline-sharding-policy
{{- end -}}