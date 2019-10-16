{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "capillary-base-cronjob.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "capillary-base-cronjob.fullname" -}}
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
{{- define "capillary-base-cronjob.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Liveness and readiness probes
*/}}
{{- define "livenessTCP" }}
          livenessProbe:
            failureThreshold: 3
            initialDelaySeconds: {{ .Values.livenessInitialDelay }}
            periodSeconds: {{ .Values.livenessPeriod }}
            successThreshold: 1
            tcpSocket:
              port: {{ .Values.livenessPort }}
            timeoutSeconds: 1
{{- end -}}

{{- define "livenessHTTPGet" }}
          livenessProbe:
            failureThreshold: 3
            initialDelaySeconds: {{ .Values.livenessInitialDelay }}
            periodSeconds: {{ .Values.livenessPeriod }}
            successThreshold: 1
            httpGet:
              path: {{ .Values.livenessHTTPEndpoint }}
              port: {{ .Values.livenessPort }}
            timeoutSeconds: 1
{{- end -}}

{{- define "readinessTCP" }}
          readinessProbe:
            failureThreshold: 3
            initialDelaySeconds: {{ .Values.readinessInitialDelay }}
            periodSeconds: {{ .Values.readinessPeriod }}
            successThreshold: 1
            tcpSocket:
              port: {{ .Values.readinessPort }}
            timeoutSeconds: 1
{{- end -}}

{{- define "readinessHTTPGet" }}
          readinessProbe:
            failureThreshold: 3
            initialDelaySeconds: {{ .Values.readinessInitialDelay }}
            periodSeconds: {{ .Values.readinessPeriod }}
            successThreshold: 1
            httpGet:
              path: {{ .Values.readinessHTTPEndpoint }}
              port: {{ .Values.readinessPort }}
            timeoutSeconds: 1
{{- end -}}