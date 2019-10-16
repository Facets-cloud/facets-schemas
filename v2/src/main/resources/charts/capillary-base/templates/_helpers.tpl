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
Liveness and readiness probes
*/}}
{{- define "livenessTCP" }}
          livenessProbe:
            failureThreshold: {{ .Values.livenessFailureThreshold }}
            initialDelaySeconds: {{ .Values.livenessInitialDelay }}
            periodSeconds: {{ .Values.livenessPeriod }}
            successThreshold: {{ .Values.livenessSuccessThreshold }}
            tcpSocket:
              port: {{ .Values.livenessPort }}
            timeoutSeconds: {{ .Values.livenessTimeout }}
{{- end -}}

{{- define "livenessHTTPGet" }}
          livenessProbe:
            failureThreshold: {{ .Values.livenessFailureThreshold }}
            initialDelaySeconds: {{ .Values.livenessInitialDelay }}
            periodSeconds: {{ .Values.livenessPeriod }}
            successThreshold: {{ .Values.livenessSuccessThreshold }}
            httpGet:
              path: {{ .Values.livenessHTTPEndpoint }}
              port: {{ .Values.livenessPort }}
            timeoutSeconds: {{ .Values.livenessTimeout }}
{{- end -}}

{{- define "readinessTCP" }}
          readinessProbe:
            failureThreshold: {{ .Values.readinessFailureThreshold }}
            initialDelaySeconds: {{ .Values.readinessInitialDelay }}
            periodSeconds: {{ .Values.readinessPeriod }}
            successThreshold: {{ .Values.readinessSuccessThreshold }}
            tcpSocket:
              port: {{ .Values.readinessPort }}
            timeoutSeconds: {{ .Values.readinessTimeout }}
{{- end -}}

{{- define "readinessHTTPGet" }}
          readinessProbe:
            failureThreshold: {{ .Values.readinessFailureThreshold }}
            initialDelaySeconds: {{ .Values.readinessInitialDelay }}
            periodSeconds: {{ .Values.readinessPeriod }}
            successThreshold: {{ .Values.readinessSuccessThreshold }}
            httpGet:
              path: {{ .Values.readinessHTTPEndpoint }}
              port: {{ .Values.readinessPort }}
            timeoutSeconds: {{ .Values.readinessTimeout }}
{{- end -}}