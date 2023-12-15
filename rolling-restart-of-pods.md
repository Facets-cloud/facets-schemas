---
title: "Rolling Restart of Service Pods"
slug: "rolling-restart-of-pods"
hidden: false
createdAt: "2022-07-23T13:23:15.738Z"
updatedAt: "2023-07-28T11:13:19.383Z"
---
With Facets, users have the ability to easily roll out and restart pods of an application through a simple button in the UI. This functionality is accomplished by running the kubectl roll out restart command in the background.

When a user clicks the "Restart" button for an application, Facets will execute this command on the corresponding Kubernetes deployment, resulting in a rolling restart of the pods associated with the deployment. This allows for a quick and easy way to restart application pods without the need for manually entering and executing the necessary kubectl commands.

This functionality is especially useful for scenarios where application pods need to be restarted to apply changes or updates to the application configuration or environment. With Facets, users can perform this operation with a simple click, reducing the potential for errors and saving time.

## Steps

1. Login to Facets Control Plane and select the Environment in your defined Blueprint.
2. Navigate to **Environment > Resources** from the sidebar and select the **Application** or **Service** in which you want to perform a rolling restart. 
3. Now, from the **Resource Details** page, select the **Kubernetes** tab.
4. Click on the **Rolling Restart All** button under the **Pods **section.
5. In the popup that appears, click **Confirm** to start the rolling restart of all pods in the application.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/42b4283-Rolling_Restart.gif",
        null,
        "Click on the image to expand"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

A success toaster is displayed if the rolling restart is initiated successfully. An error toaster with an appropriate error message is displayed if the rolling restart fails.