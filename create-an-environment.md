---
title: "Create an Environment"
slug: "create-an-environment"
hidden: false
metadata: 
  title: "Launch an Environment - Facets DevOps Platform"
  description: "To create an environment, simply click on the  \"New Environment\" button in the blueprint of the Facets DevOps Platform"
createdAt: "2022-03-09T19:48:23.011Z"
updatedAt: "2023-07-26T06:14:52.969Z"
---
After creating your Blueprint, you can now launch the first manifestation of it, which is the **Environment. **

1. Navigate to **Blueprint > Overview**, and you will see a prompt to create your first Environment.
2. **Select the Cloud Provider:** The next step is to select the cloud provider. This will open a 3-step stepper wizard.
3. **Basic Information: **In the first step, you need to name your environment, select its timezone, and select its release stream. This information will be relevant when wiring builds.
4. **Cloud-Specific Information:** In the second step, you need to specify the CIDR range for the VPC, making sure not to conflict with existing clusters if you have a use case for peering. Additionally, you need to select the region, AZ, and node family.
5. **Link Cloud Account with Facets:** In the third step, you need to link the cloud account with the Facets control plane or select a pre-configured cloud account.
6. Click **Create.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/9e30682-Create_an_Environment.gif",
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

A logical environment would be created by clicking the **Create** button. However, please note that the environment will not be launched immediately and will appear as 'stopped' on the environment page.