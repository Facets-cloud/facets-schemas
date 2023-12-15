---
title: "Integrating with CI"
slug: "ci-integration"
excerpt: "How Facets Control Plane Integrates with CI systems"
hidden: true
createdAt: "2022-03-23T18:26:03.022Z"
updatedAt: "2023-02-14T20:45:30.179Z"
---
# Representation

In earlier sections, we described how to push a build manually to Facets Control Plane. In this Section, you will learn how to automate pushing the container builds to Facets Control Plane from your CI system. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/be71aa7-Facets_Graphic_Image.png",
        "Facets Graphic Image.png",
        1920
      ],
      "align": "center",
      "caption": "CI Integration"
    }
  ]
}
[/block]

# Release Streams

Release streams are logical groups of environment types. The available types are as QA, QA2, STAGING, PROD. You can view / modify the release stream in the edit 

![](https://files.readme.io/c72e926-Screen_Shot_2022-03-24_at_5.21.09_PM.png "Screen Shot 2022-03-24 at 5.21.09 PM.png")

# Integrating with Jenkins

## :one: Set Global Environment Variable

Navigate to “Manage Jenkins -> Configure System -> Global properties option”. Check the “Environment variables” checkbox and then add the following variables

1. $CP_URL - Your Control Plane URL.  
2. $CC_TOKEN - Your Authentication token.
3. $CC_USER - Authorized username in your Control Plane

## :two: Create or edit your Job

Go about the regular Jenkins build process. 

## :three: Set up the post-build hook to call Facets

In this example, we will be pushing the master build in to the QA2 stream. :point-down:  


[block:tutorial-tile]
{
  "backgroundColor": "#018FF4",
  "emoji": "🦉",
  "id": "638dcfb3e5003e002bee7e35",
  "link": "https://facets.readme.io/v0.5/recipes/jenkins-post-build-step-to-push-an-image-to-facets-control-plane-automatically",
  "slug": "jenkins-post-build-step-to-push-an-image-to-facets-control-plane-automatically",
  "title": "Jenkins Post-build Step to push an Image to Facets Control Plane Automatically"
}
[/block]