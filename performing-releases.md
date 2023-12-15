---
title: "Performing Releases"
slug: "performing-releases"
hidden: false
createdAt: "2022-12-05T11:57:27.736Z"
updatedAt: "2023-08-24T10:46:55.175Z"
---
Let's follow the quick steps to perform a release. 

## Steps

1. Navigate to **Environment > Releases** from the navigation pane.
2. Click on the **Release** button.
3. In the modal that appears, select the [type of release](https://readme.facets.cloud/v1.0/docs/releases-concept#introduction) (Full Release, Selective Release, Custom Release) and add comments if any.
4. For Full Release, enable the required toggle buttons in the **Advanced Options.** Click **Apply** or **Plan.**

- **"Apply" and "Plan"** are two options that you can choose while deploying a release in Facets.
- **"Apply"** refers to the process of actually executing the deployment according to the blueprint. This option will immediately start the deployment process and any changes made will take effect in the environment.
- **"Plan"** is a validation check before deploying a release. It allows you to preview the changes that will take place in the environment before actually applying them. The "Plan" option will generate a report that shows what changes will be made, and you can then choose to "Apply the Plan" to execute the deployment.

5. For Selective Release, you have the option to **Group by** **Resource Type** or **Resource Group,** and based on that select the desired resources or resource groups you wish to deploy, then click **Apply.**
6. For Custom Release, add custom commands and enable the required toggle buttons in the **Advanced Options.** Edit or delete the custom command if necessary. Click **Apply.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/84e4f94-releases.gif",
        null,
        null
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

## Advanced Options

The **Advanced Options** section is available for both **Full Release** and **Custom Release** deployment types. This section allows you to enable certain advanced options that can impact the deployment process.

The advanced options in Facets include:

- **Refresh:** When enabled, the release will detect any infrastructure drifts and update resource information in the backed-up database.
- **Force:** A release that is executed with the "Force" option enabled will override any existing resources in the environment. This can be useful when there are conflicting resource definitions in the blueprint.
- **Delete:** When enabled, this option will delete any resources in the environment that are not defined in the blueprint. This can be useful for cleaning up any obsolete resources in an environment.

These advanced options can be used to fine-tune the deployment process and ensure it is executed as expected. It is important to understand the impact of these options before enabling them, as they can potentially cause unexpected behavior in the environment.