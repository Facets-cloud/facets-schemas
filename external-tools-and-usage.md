---
title: "External Tools"
slug: "external-tools-and-usage"
excerpt: "Understand the out of the box tools provided by Facets and their use cases."
hidden: false
createdAt: "2022-07-18T19:00:54.501Z"
updatedAt: "2023-02-21T10:28:04.227Z"
---
## Introduction

Facets offers a variety of external tools to help you monitor, configure, and manage the resources in your control plane. These tools are designed to work together to provide a comprehensive solution for resource management.

Below is a list of the tools that are currently available in Facets:

- Kubernetes Embedded Dashboard: Allows users to interact dynamically with Kubernetes-related actions.
- Prometheus: A monitoring system and time-series database.
- Alertmanager: Handles alerts sent by Prometheus and other client applications.
- Grafana: An open-source analytics and visualization tool.
- Wetty: A terminal emulator that enables you to access your terminal directly from the web.

In addition to these tools, Facets also allows users to integrate other tools based on their specific needs, providing even more flexibility and control over resource management.

## Kubernetes Embedded Dashboard

The Kubernetes Embedded Dashboard provides a dynamic way to interact with Kubernetes-related actions directly from the Facets UI. This eliminates the need to download and refresh kubeconfig, allowing you to use the embedded dashboard for all your Kubernetes-related actions.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/a52cdd2-embedded_k8s.png",
        null,
        "Kubernetes Dashboard screen (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px"
    }
  ]
}
[/block]



## Prometheus

Prometheus is a reliable and simple open-source monitoring and alerting system. The alerting rules are set in Prometheus and the alerts are managed in Alertmanager. Prometheus collects time-series metrics from monitored targets, and the data is collected and analyzed accurately by Prometheus due to its use of key-value pairs that resemble Kubernetes' use of labels to organize infrastructure metadata.

## Alertmanager

Alertmanager handles alerts sent by Prometheus and other client applications. It deduplicates, groups, and routes alerts to the appropriate receiver integration based on their labels. Alertmanager also manages the silencing and inhibition of alerts. Receiver integration methods include email, Slack, or custom integration through a generic webhook interface.

## Grafana

Grafana provides a comprehensive toolset for turning time-series database data into insightful graphs and visualizations. It supports over 15 popular databases and monitoring solutions, and the metrics collected from your data sources are displayed in approachable dashboards, making it easy for anyone to understand.