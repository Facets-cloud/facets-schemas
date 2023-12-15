---
title: "Creating a Promotion Workflow"
slug: "creating-a-promotion-workflow"
hidden: false
createdAt: "2023-03-17T10:40:45.275Z"
updatedAt: "2023-09-06T04:09:48.378Z"
---
A Promotion Workflow allows you to promote changes through different environments/release streams, ensuring a streamlined and controlled process for software deployment. This document provides a step-by-step guide for creating a promotion workflow.

1. Navigate to **Artifacts > Promotion Workflow.**  
   You will find all the **Promotion Workflows **created in the Facets Control Plane on this page.
2. Click **Create Promotion Workflow** and mention the **Workflow Name.**

## Registration Type

Select the **Registration Type** as **Environment Name **or** Release Stream.**

### Environment Name

1. If you choose the **Registration Type **as** Environment Name,** all the **Available Environments** will be displayed under **Promotional Hierarchy.**
2. You can also click the **+** icon beside the environment or drag the environments from the **Available Environments** to the **Environments Hierarchy** column to create the **Promotion Hierarchy.**
3. Click **Create.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/af7181e-image.png",
        null,
        "Registration Type as Environment Name"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

### Release Stream

1. If you choose the **Registration Type **as** Release Stream,** all the **Available Release Streams** will be displayed under **Promotional Hierarchy.**
2. You can also click the **+** icon beside the release stream or drag the release stream from the **Available Release Streams** to the **Release Streams Hierarchy** column to create the **Promotion Hierarchy.**
3. **Global: **Enabling this toggle will make this workflow available to all the blueprints.
4. Click **Create.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/38845cc-image.png",
        null,
        "Registration Type as Release Stream"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

You have successfully created a Promotion Workflow in the Facets Control Plane.

## Related guides

- [Promoting an Artifact](https://readme.facets.cloud/docs/creating-a-ci-integration#promoting-an-artifact)