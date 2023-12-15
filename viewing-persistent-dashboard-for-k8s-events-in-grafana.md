---
title: "Viewing Kubernetes events"
slug: "viewing-persistent-dashboard-for-k8s-events-in-grafana"
hidden: false
createdAt: "2022-08-04T05:21:46.832Z"
updatedAt: "2023-07-20T09:31:31.495Z"
---
Facets Control Plane enables users to view Kubernetes Events in the Grafana Dashboard and filter them on the basis of namespace, kind, and name of the object.

The primary use case for this dashboard is to allow users to view the health of an application running inside a Kubernetes cluster directly from the Control Plane.

## Steps

1. Navigate to **Blueprint > List** and select a **Blueprint.**
2. Select the **Environment** in your defined **Blueprint** where you want to view the persistent dashboard.
3. Now, navigate to **Environment > Tools** from the sidebar and select the **Grafana** tab.
4. Click on the **Search** button, search for **Kubernetes Events** dashboard in the search menu, and open the same.
5. Choose the appropriate values for **ObjectKind**, **ObjectName**, **ObjectNamespace** fields.
6. You can now view the Events in a tabular format.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/f9b475d-Kubernetes_events.gif",
        null,
        "Click to expand"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]