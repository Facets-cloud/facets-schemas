---
title: "Integrating with Okta"
slug: "integrating-with-okta"
hidden: false
createdAt: "2023-05-12T06:17:17.379Z"
updatedAt: "2023-05-25T10:27:24.042Z"
---
## Creating an Application in Okta

1. Log in to your Okta console and navigate to **Applications > Applications.**
2. Click **Create App Integration.**
3. In the pop-up that appears, select **OIDC - OpenID Connect** as the **Sign-in method** and **Web Application** as the **Application type.**
4. You will be redirected to a **New Web App Integration **window.
   1. Under **General Settings,** mention the **App integration name.**
   2. Enter the **Sign-in redirect URIs.**  
      **Eg: **_`https://<control-plane-url>/login/oauth2/code/<registration-ID>`_
      > 📘 
      > 
      > - Replace `<control-plane-url>` in the above link with your organization's Control Plane URL.
      > - Note down the `<registration-ID>`, as the same value needs to be entered in your Control Plane.
   3. Under **Assignments,** select **Skip group assignment for now** under **Controlled access.**
5. Click **Save.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/62e338d-okta.gif",
        null,
        "Create App Integration in Okta"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Creating an App in Okta"
    }
  ]
}
[/block]

You have successfully created an Application in Okta.

## Adding integration for Okta in Facets

1. Navigate to **Setting > OAuth Integrations** from the left pane.  
   This page displays all the configured OAuth Integrations in Facets.
2. Click **Add Integrations.**
3. Now, select **Okta** as the **Provider.**
4. Fill in the following information:
   1. **Registration ID**
   2. **Client ID**
   3. **Client Secret**
   4. **Issuer URL**  
      a. Log in to the Okta console and navigate to **Security > API.**  
      b. Under **Authorization Servers,** you will find the **Issuer URI.**
   5. **Login Button Text:** Choose the text that will appear in the Control Plane Login screen. Facets recommends using your organization's name.
5. Click **Create. **

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/9c2079b-image.png",
        null,
        "Okta Configuration Screen"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Okta Configuration Screen"
    }
  ]
}
[/block]

You have successfully set up Okta integration in Facets.