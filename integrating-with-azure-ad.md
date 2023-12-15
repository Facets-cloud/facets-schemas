---
title: "Integrating with Azure AD"
slug: "integrating-with-azure-ad"
hidden: false
createdAt: "2023-09-14T07:28:51.696Z"
updatedAt: "2023-09-22T06:55:09.424Z"
---
Facets now supports OAuth integration with Azure AD. This guide will walk you through the steps required to configure and enable Azure AD OAuth integration within Facets.

## Registering an Application in Azure AD

1. Login to your [Azure portal](https://portal.azure.com/) and under **Azure Services**, select **Azure Active Directory.**
2. Under **Manage,** select **App Registrations** from the left pane. 
3. Click **New Registration. **
4. In the **Register an application** page, do the following:
   1. Mention the **Name **and select the **Supported account types.**
   2. In **Redirect URI,** under **Select a platform,** select **Web **and mention the URI in the below format.  
      `https://<control-plane-url>/login/oauth2/code/<registration-ID>`  
      **For example:**`https://demo.facets.cloud/login/oauth2/code/facetsdemo`
   3. Click **Register.**

> 📘 
> 
> - Replace `<control-plane-url>` in the above link with your organization's Control Plane URL.
> - The value of `<registration-ID>` provided in the URL needs to be used in the Control Plane. Please make a note of it. Additionally, please ensure that the `<registration-ID>` does not contain any spaces or special characters.

You have successfully registered your application in Azure AD.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/a16c309-Reg_an_app_in_Azure_AD.gif",
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


## Adding integration for Azure AD in Facets

1. Navigate to **Setting > OAuth Integrations** from the left pane.  
   This page displays all the configured OAuth Integrations in Facets.
2. Click **Add Integrations** and select **Azure AD** as the **Provider.**
3. Fill in the following information:
   1. **Registration ID**
   2. **[Client ID](doc:integrating-with-azure-ad#get-client-id-and-tenant-id)**
   3. **[Client Secret](doc:integrating-with-azure-ad#get-client-secret)**
   4. **Issuer URL:** Mention the **[Tenant ID](doc:integrating-with-azure-ad#get-client-id-and-tenant-id).**
   5. **Login Button Text:** Choose the text that will appear in the Control Plane Login screen. Facets recommends using "AD Login".
4. Click **Create.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/db1b083-image.png",
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


You have successfully set up Azure AD integration in Facets.

### Get Client ID and Tenant ID

1. Log in to the Azure Portal and navigate to the registered application.
2. In the application** Overview,** you will find the **Application (client) ID** and the **Directory (tenant) ID.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/e39d728-image.png",
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


### Get Client Secret

1. Log in to the Azure Portal and navigate to the registered application.
2. In the application, select **Certificates and Secrets** from the left pane.
3. In the **Client Secrets** tab, click **New client secret.**
4. Mention the **Description** and the **Expiry.** 
5. Click **Add.** 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/6e6f7dc-image.png",
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


6. Now, copy and use the value from the **Value** column of the added client secret.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/2b6ab35-image.png",
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


> 📘 
> 
> Please note that this **Value** will be visible for only a few minutes; therefore, we recommend copying and saving it for your reference.

You have successfully created a new **Client Secret.**