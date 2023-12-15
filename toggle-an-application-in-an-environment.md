---
title: "Disable/Enable an Application in an Environment"
slug: "toggle-an-application-in-an-environment"
excerpt: "Learn how to selectively enable / disable apps per environment for performing a Release"
hidden: true
createdAt: "2022-06-27T04:15:11.345Z"
updatedAt: "2022-11-29T05:31:16.391Z"
---
The primary use case for selectively enabling or disabling an application in an environment is to either add or remove that particular application in the next release. This is achieved by overriding the Container Configuration for an application from its Configuration Screen.

## Prerequisites

An application should be defined in the blueprint specification in the environment where you want to override its current configuration.

## Disable an Application from your Control Plane

1. Login to Facets Control Plane. 
2. Select the **Blueprint **which has your **Environment**.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/dcede10-blueprint_page.png",
        "blueprint page.png",
        2560
      ],
      "sizing": "80",
      "border": true,
      "caption": "Select the Blueprint which has the environment with your application (Click on the image to expand)"
    }
  ]
}
[/block]

3. Navigate to **Applications **page from the sidebar and click on the Application in question.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/123da19-apps_backend.png",
        "apps backend.png",
        2554
      ],
      "sizing": "80",
      "border": true,
      "caption": "Select the Application whose Container Configurations needs to be overridden. (Click on the image to expand)"
    }
  ]
}
[/block]

4. Click on the **Override **button in the Configuration tab.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/3eda39c-override_configuration.png",
        "override configuration.png",
        2163
      ],
      "sizing": "80",
      "border": true,
      "caption": "Click on the Override button. (Click on the image to expand)"
    }
  ]
}
[/block]

5. Choose whether you want to disable this application from being included in the next release by ticking the **Disabled **checkbox, and clicking on the **Submit **button at the bottom of the screen. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/e020f05-disabled.png",
        "disabled.png",
        2014
      ],
      "sizing": "80",
      "border": true,
      "caption": "Tick the Disabled checkbox. (Click on the image to expand)"
    }
  ]
}
[/block]

6. You will see a popup window asking you to **Review Changes**. Click on **Submit Changes** to override the configuration.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/3547088-review_changes.PNG",
        "review changes.PNG",
        1881
      ],
      "sizing": "80",
      "border": true,
      "caption": "Review the Changes and Submit. (Click on the image to expand)"
    }
  ]
}
[/block]

7. You will see a Toaster popup confirming the change and a notification that this change will be applied in the next release.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/19cccf8-disabled_popup.png",
        "disabled popup.png",
        2034
      ],
      "sizing": "80",
      "border": true,
      "caption": "Success Message. (Click on the image to expand)"
    }
  ]
}
[/block]

## Enable an Application from your Control Plane

> 📘 Enabling an Application
> 
> You can follow the same steps for enabling a disabled application by deselecting the tick box in Step 5 and submitting the change.

## Constraints

The change of state of an application as toggled by a user will only be enforced on the next release of the environment.