---
title: "Manage Infrastructure as Code (IaC)"
slug: "manage-iac-versions"
excerpt: "Learn how to change IaC major, minor version and stream from your Control Plane"
hidden: true
createdAt: "2022-08-18T04:54:44.811Z"
updatedAt: "2023-02-14T21:06:45.311Z"
---
Facets provides users with the capability to change the major and minor IaC version and stream directly from the Control Plane. 

Table of Contents

- [IaC in Facets](https://readme.facets.cloud/docs/manage-iac-versions#iac-in-facets)
- [Prerequisites to manage IaC versions](https://readme.facets.cloud/docs/manage-iac-versions#prerequisites-to-manage-iac-versions)
- [Manage IaC Version from your Control Plane](https://readme.facets.cloud/docs/manage-iac-versions#manage-iac-version-from-your-control-plane)
- [Constraints](https://readme.facets.cloud/docs/manage-iac-versions#constraints)

## IaC in Facets

Infrastructure as Code (IaC) is the managing and provisioning of infrastructure through your Control Plane instead of through manual processes.

Facets provides these three different streams of IaC in your Control Plane for consistent, error free deployments:

- Production
- Staging
- Development

### Use Cases

By using IaC versions, users can get the following advantages over traditional manual deployments:

- Fixed version for your IaC  
  Maintaining consistency across environments is as simple as deploying with a fixed IaC version and provides the additional benefit of having immutable infrastructure.
- Staging IaC as a precursor for upcoming changes  
  Users can test their environments reliably in the Staging IaC, to ensure zero down-time when they deploy resources in Production IaC.
- Major version upgrade  
  Users can update their environments in a safe manner by testing and deploying resources using a major version of IaC in the staging stream, then migrating the neccessary environments to the next major version.

## Prerequisites to manage IaC versions

- An environment with resources must exist in the blueprint where your infrastructure has been added in the definition. 

> 📘 NOTE
> 
> If there is no defined IaC version for an environment during deployment, Facets will automatically select the latest version of IaC from `PRODUCTION` stream.

## Manage IaC Version from your Control Plane

Facets allows you to manage the IaC version from your **Environment Overview** screen as well as **Releases** screen. 

> 📘 NOTE
> 
> IaC version or stream change will take effect from the next release after the change.

### Using Environment Overview screen

1. Login to your **Control Plane**.
2. Select a **Blueprint**.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/9b744c6-select_blueprint.png",
        null,
        "Select a Blueprint (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Select a Blueprint (Click on the image to expand)"
    }
  ]
}
[/block]

3. Select the **Environment** in your defined **Blueprint** where you want to manage IaC version.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/2f75e47-select_env.png",
        null,
        "Select an Environment (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Select an Environment (Click on the image to expand)"
    }
  ]
}
[/block]

4. In the Environment Overview page, click on **EDIT** button in the **IaC Version** widget.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/06f06b5-edit_iac_in_env.png",
        null,
        "Edit button in IaC Version widget (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Edit button in IaC Version widget (Click on the image to expand)"
    }
  ]
}
[/block]

5. Fill in the changes in the **Edit IaC Version** window that appears on screen, and click **Save Changes** to apply your changes.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/d9a95ad-edit_iac_popup.png",
        null,
        "Edit IaC Version popup window (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Edit IaC Version popup window (Click on the image to expand)"
    }
  ]
}
[/block]

6. You should see a success toaster popup confirming your changes. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/609c1f6-success_iac.png",
        null,
        "Success toaster (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Success toaster (Click on the image to expand)"
    }
  ]
}
[/block]

### Using Releases screen

> 📘 
> 
> Follow **Steps 1-3** from the [Using Environment Overview screen](https://readme.facets.cloud/docs/manage-iac-versions#using-environment-overview-screen) section.

4. Navigate to Releases screen using the sidebar navigation.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/e11e2c8-releases_page.png",
        null,
        "Click on the Releases button in the Navigation bar (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Click on the Releases button in the Navigation bar (Click on the image to expand)"
    }
  ]
}
[/block]

5. Click on the Ellipsis button to view more options and select **Update IaC Version**.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/b6185b3-update_iac_2.png",
        null,
        "Update IaC Version button in other settings (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Update IaC Version button in other settings (Click on the image to expand)"
    }
  ]
}
[/block]

6. Fill in the changes in the **Edit IaC Version** window that appears on screen, and click **Save Changes** to apply your changes.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/539cdbc-update_iac_popup_2.png",
        null,
        "Edit IaC Version popup window (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Edit IaC Version popup window (Click on the image to expand)"
    }
  ]
}
[/block]

7. You should see a success toaster popup confirming your changes. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/5c96647-success_iac.png",
        null,
        "Success toaster (Click on the image to expand)"
      ],
      "align": "center",
      "sizing": "450px",
      "caption": "Success toaster (Click on the image to expand)"
    }
  ]
}
[/block]

## Constraints

- The IaC version change will take effect from the next release.
- For the major version, user can only update to a newer upgraded version. It not possible to move to a lower version. For example, if a user is using major version 1, it is not possible to move to 0.