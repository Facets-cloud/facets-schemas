---
title: "Resource Type - Application"
slug: "resource-type-application"
hidden: true
createdAt: "2023-01-19T04:07:08.647Z"
updatedAt: "2023-01-23T09:11:57.047Z"
---
Resources of type Application are different from other resources. This document outlines the Resource Details page for applications.

1. Navigate to **Environment >Resources.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/21a8358-image.png",
        null,
        "Resources tab in left Navigation menu"
      ],
      "sizing": "350px",
      "border": true,
      "caption": "Resources tab in left Navigation menu"
    }
  ]
}
[/block]

2. Click on the resource name to view the details of the application resource.
3. On this page, you will find four tabs namely,

- Live Release
- Configuration
- Diff
- Grafana Dashboard

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/c1ed46e-image.png",
        null,
        "Resource Details screen"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Resource Details screen"
    }
  ]
}
[/block]

## Live Release

This page displays all the details regarding this live release. 

### Current Registered Artifact

The Current Registered Artifact widget displays the latest active artifact. 

1. Click the **Artifact History** button on the top right corner.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/03e242b-image.png",
        null,
        "Artifact History navigation"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Artifact History navigation"
    }
  ]
}
[/block]

2. The **Artifact History** page appears. Here, you can view the **Currently Registered version** and **Previous Versions. **
3. Click the **Artifact rollback **icon beside the required version to roll back to that Artifact version. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/8349d27-image.png",
        null,
        "Artifact rollback navigation"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Artifact rollback navigation"
    }
  ]
}
[/block]

4. In the pop-up that appears, click **Confirm. **

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/5aeff85-image.png",
        null,
        "Confirm Artifact Rollback pop-up"
      ],
      "sizing": "350px",
      "border": true,
      "caption": "Confirm Artifact Rollback pop-up"
    }
  ]
}
[/block]

You have successfully performed an artifact rollback.

### Line Graphs

This depicts all the CPU, Memory, Pods, and Restarts in a line graph.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/b244ce4-image.png",
        null,
        "CPU usage graph"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "CPU usage graph"
    }
  ]
}
[/block]

### Horizontal Pod Autoscaling

This widget displays the Current CPU Utilization(%) and the number of pods in this environment. 

### Pods

This displays all the pods that are available in this application resource. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/54c48b8-image.png",
        null,
        "Available Pods"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Available Pods"
    }
  ]
}
[/block]

**Tail Logs**

1. Click the **Tail Logs** button on the top right corner of the pod.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/42d5f65-image.png",
        null,
        "Tail Logs navigation"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Tail Logs navigation"
    }
  ]
}
[/block]

2. In the pop-up that appears, you will be able to view the tail logs of the Terraform Logs for that pod.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/80dbf1a-image.png",
        null,
        "Tail Logs Screen"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Tail Logs Screen"
    }
  ]
}
[/block]

3. Click on the clipboard icon in the top right corner to copy the logs to the clipboard.

**Rolling Restart All**

The Rolling Restart All allows you to sequentially restart all the available pods in that application resource.

1. Click the **Rolling Restart All **button.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/d4ed9a1-image.png",
        null,
        "Rolling Restart All navigation"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Rolling Restart All navigation"
    }
  ]
}
[/block]

2. In the pop-up that appears, click **Confirm.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/0c0b496-image.png",
        null,
        "Confirm Rolling Restart All pop-up"
      ],
      "sizing": "350px",
      "border": true,
      "caption": "Confirm Rolling Restart All pop-up"
    }
  ]
}
[/block]

You have successfully restarted the pods. 

### Ingress Rules

This widget displays all the Ingress rules configured with this resource.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/5224862-image.png",
        null,
        "Ingress Rules widget"
      ],
      "sizing": "350px",
      "border": true,
      "caption": "Ingress Rules widget"
    }
  ]
}
[/block]

## Configuration

This page displays the configurations effective in this environment. You will also find the **Override** and **Version History** buttons. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/7fb8445-image.png",
        null,
        "Configuration tab"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Configuration tab"
    }
  ]
}
[/block]

### Override

This allows users to override any defined key-value pairs for this resource without making any changes to the blueprint.

1. Click the **Override** button to view the Container Configurations and user-defined variables if any.
2. Using the **Container Configurations** form, you will be able to make the necessary changes to the application.
3. To have access to the complete blueprint of the application, click **JSON Mode** from the top right corner of the screen. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/8ed1af0-image.png",
        null,
        "Override Configuration Form mode"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Override Configuration Form mode"
    }
  ]
}
[/block]

4. On the** JSON Mode** page, you will find two columns. The left column consists of the defined blueprint and the right column contains the override command. 
5. Enter the override command in the right column and click **Submit** to save the changes. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/69a1723-image.png",
        null,
        "Override Configuration JSON mode"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Override Configuration JSON mode"
    }
  ]
}
[/block]

> 📘 Note
> 
> Any changes made to the blueprint using override from this page is specific to this particular application for this environment only.

You have successfully overridden the blueprint for this application. 

### Version History

This page displays all the configured versions of this resource.

1. Click the **View changes **icon beside the desired version to view the changes made to this version with the previous version.  

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/f5d806e-image.png",
        null,
        "Resource Configuration Version History Screen"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Resource Configuration Version History Screen"
    }
  ]
}
[/block]

2. Click the **Compare with current version** icon beside the desired version to compare the older versions to the current version. This icon is only available under the **Previous Versions** widget.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/b794fee-image.png",
        null,
        "Comparing versions pop-up"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Comparing versions pop-up"
    }
  ]
}
[/block]

## Diff

On this page, you will be able to compare this environment with any other environment. Note that you will be able to select up to 2 environments to compare.

1. **Select environment(s)** and click **Compare.**

> 📘 Note
> 
> You will only be able to select between the environments in which this application resource is defined.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/44d38ca-image.png",
        null,
        "Compare Environments navigation"
      ],
      "border": true,
      "caption": "Compare Environments navigation"
    }
  ]
}
[/block]

2. Now a screen comparing the selected environment appears. This compares and displays any differences between the application in the selected environments.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/98e3461-image.png",
        null,
        "Compare Environments screen"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Compare Environments screen"
    }
  ]
}
[/block]

## Grafana Dashboard

This page displays the application details in a dashboard.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/b3f5e87-image.png",
        null,
        "Grafana Dashboard"
      ],
      "sizing": "700px",
      "border": true,
      "caption": "Grafana Dashboard"
    }
  ]
}
[/block]

If the status of the application is in the failed state, click the **Manage your Environment** button. This navigates you to the Releases page where you can plan and deploy a release. 

To know more about releases, refer to the [Releases](doc:releases-concept) documentation.

## Related Guides

- [Resource Center](doc:resource-center)
- [Resource Details](doc:resource-details)