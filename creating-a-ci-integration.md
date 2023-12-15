---
title: "Creating a CI Integration"
slug: "creating-a-ci-integration"
hidden: false
createdAt: "2023-05-09T07:40:43.222Z"
updatedAt: "2023-08-01T06:42:35.110Z"
---
This document provides a step-by-step guide for creating a CI Integration.

## Create a CI Integration

1. **Access the CI Integration Page:** From the dashboard, click on **Artifacts**, then select **CI Integration.** Here, you'll find all the CI Integrations created within the Facets Control Plane.
2. **Create a New CI Integration:** Click the **Create CI Integration** button.
3. **Name Your CI Integration:** Enter your desired name in the **CI Integration Name** field.  
   **Note:** The CI Integration name should be identical to the corresponding Artifact name for consistency.
4. **Select Registration Type:** Choose between **Environment Name** and **Release Stream.**
5. **Set Global Access:** If you have multiple blueprints, enabling this option allows your CI Integration to be accessible across all Blueprints.
6. **Configure Advanced Options:** Under the **Advanced Options** section, you can configure the promotion hierarchy by selecting the **Promotion Workflow** to define the desired workflow for promoting artifacts. Additionally, you can classify artifacts by choosing the appropriate **CI Rule.**
7. **Finalize Creation:** Click **Create** to finish setting up your CI Integration. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/c92f930-CI_Integration.gif",
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

The new CI Integration will appear under the '**CI Integration**' tab.

## Pushing an Artifact

1. **Access the CI Integration Details Page:** Navigate to the CI Integration page where you intend to push the artifact.
2. **Select the Release Stream or Environment:** Locate the Release Stream or Environment where you want to push the artifact. Click the **Push Artifact** icon found under the **Actions** column.
3. **Push the Artifact:** In the pop-up window that appears, the Facetsctl command required to push your artifact will be displayed.
4. For a comprehensive understanding and proper usage of the push command, make sure to refer to the [Facets CLI](https://readme.facets.cloud/docs/command-line-tool-for-facets) documentation.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/04e1332-image.png",
        null,
        "Push an Artifact"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

You have successfully pushed an artifact.

## Promoting an Artifact

1. **Choose the Artifact: **Locate the Environment or Release Stream containing the artifact you wish to promote, then click the **Promote Artifact** icon in the **Actions** column.
2. **Promote the Artifact: **In the pop-up window that appears, click **Promote.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/d83d64d-image.png",
        null,
        "Promote an Artifact"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Click on the image to expand"
    }
  ]
}
[/block]

You have successfully promoted the artifact.

## Related Guide

- [Creating CI Rules](doc:ci-rules)