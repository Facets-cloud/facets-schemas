---
title: "Map Custom Domains"
slug: "map-custom-domains"
hidden: false
createdAt: "2023-08-11T06:04:03.434Z"
updatedAt: "2023-08-30T19:52:01.848Z"
---
Domain mapping allows businesses to associate their custom domain with their online presence, enhancing branding and accessibility.

1. Login to Facets Control Plane and select the Environment in your defined Blueprint.
2. Navigate to **Environment > Resource Centre.**
3. In the Resource Centre, locate and select the Ingress resource to which you want to map the custom domain.
4. Click on the **Domains** tab in the Resource Details section and click **Map Custom Domain.**
5. In the UI that appears, provide the following details:
   - **Custom Domain Alias: **This unique, user-defined name serves as an alternative web address. 
   - **Custom Domain URL: **This refers to the specific web address associated with a Custom Domain Alias.
   - **Certificate Reference: **This unique identifier points to a security certificate stored in your system.
6. Click **Save.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/642b76f-mapping_domains.gif",
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

You have successfully mapped a custom domain.

7. Now, navigate to your DNS provider, establish a CNAME record for the designated custom domain, and save the changes.