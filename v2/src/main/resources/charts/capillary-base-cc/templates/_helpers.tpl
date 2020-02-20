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
            failureThreshold: {{ .Values.liveness.livenessFailureThreshold }}
            initialDelaySeconds: {{ .Values.liveness.livenessInitialDelay }}
            periodSeconds: {{ .Values.liveness.livenessPeriod }}
            successThreshold: {{ .Values.liveness.livenessSuccessThreshold }}
            tcpSocket:
              port: {{ .Values.liveness.livenessPort }}
            timeoutSeconds: {{ .Values.liveness.livenessTimeout }}
{{- end -}}

{{- define "livenessHTTPGet" }}
          livenessProbe:
            failureThreshold: {{ .Values.liveness.livenessFailureThreshold }}
            initialDelaySeconds: {{ .Values.liveness.livenessInitialDelay }}
            periodSeconds: {{ .Values.liveness.livenessPeriod }}
            successThreshold: {{ .Values.liveness.livenessSuccessThreshold }}
            httpGet:
              path: {{ .Values.liveness.livenessHTTPEndpoint }}
              port: {{ .Values.liveness.livenessPort }}
            timeoutSeconds: {{ .Values.liveness.livenessTimeout }}
{{- end -}}

{{- define "readinessTCP" }}
          readinessProbe:
            failureThreshold: {{ .Values.readiness.readinessFailureThreshold }}
            initialDelaySeconds: {{ .Values.readiness.readinessInitialDelay }}
            periodSeconds: {{ .Values.readiness.readinessPeriod }}
            successThreshold: {{ .Values.readiness.readinessSuccessThreshold }}
            tcpSocket:
              port: {{ .Values.readiness.readinessPort }}
            timeoutSeconds: {{ .Values.readiness.readinessTimeout }}
{{- end -}}

{{- define "readinessHTTPGet" }}
          readinessProbe:
            failureThreshold: {{ .Values.readiness.readinessFailureThreshold }}
            initialDelaySeconds: {{ .Values.readiness.readinessInitialDelay }}
            periodSeconds: {{ .Values.readiness.readinessPeriod }}
            successThreshold: {{ .Values.readiness.readinessSuccessThreshold }}
            httpGet:
              path: {{ .Values.readiness.readinessHTTPEndpoint }}
              port: {{ .Values.readiness.readinessPort }}
            timeoutSeconds: {{ .Values.readiness.readinessTimeout }}
{{- end -}}