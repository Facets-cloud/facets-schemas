---
title: "Manage Infrastructure as Code (IaC)"
slug: "manage-iac"
hidden: false
createdAt: "2023-04-06T10:19:16.913Z"
updatedAt: "2023-09-26T11:26:35.219Z"
---
Facets provides users with the capability to change the stream, and major and minor IaC version directly from the Control Plane. 

Learn more about [IaC in Facets](doc:iac-using-facets).

## Manage IaC Version from your Control Plane

Facets allows you to manage the IaC version from your **Environment Overview** screen as well as **Releases** screen. 

> 📘 
> 
> **Note: **IaC version or stream change will take effect from the next release after the change.

1. Login to your **Control Plane.**
2. Navigate to **Blueprint > List** and select a **Blueprint.**
3. Select the **Environment** in your defined **Blueprint** where you want to manage the IaC version.

### Using the Environment Overview screen

1. In the **Environment Overview** page, click the **Edit** button in the **IaC Version** widget.
2. In the **Edit IaC Version **popup, make the required modifications and then click **Save Changes.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/9c25a57-Manage_IaC_-_env_overview.gif",
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


You should see a success toaster popup confirming your changes.

### Using the Releases screen

1. Navigate to **Environment > Releases.**
2. Click on the Ellipsis button to view more options and select **Update IaC Version.**
3. In the **Edit IaC Version **popup, make the required modifications and then click **Save Changes.**

[block:image]{"images":[{"image":["https://files.readme.io/8cfe618-Manage_IaC_-_releases.gif",null,"Click on the image to expand"],"align":"center","sizing":"450px","border":true,"caption":"Click on the image to expand"}]}[/block]

You should see a success toaster popup confirming your changes.

> 📘 NOTE
> 
> **Note: **If there is no defined IaC version for an environment during deployment, Facets will automatically select the latest version of IaC from `PRODUCTION` stream.