---
title: "Part 1 - Write and Deploy the App"
slug: "part-1-deploy-the-flask-app"
hidden: true
createdAt: "2022-03-30T17:18:34.506Z"
updatedAt: "2022-04-04T11:31:39.652Z"
---
#Step 1: Create a blueprint directory and files.

The starting point of interacting with Facets Control Plane is the creation of a <<glossary:blueprint>>. A blueprint is a git version controlled collection of json files. Each of this json files represents an intent and defines the resources required and their configurations and dependencies. 

For our demo application, we only need an application instance and an ingress to access the application from the outside world. There are 2 parts to creating a blueprint in Facets Control Plane.

1. First is the creation of json files in a pre-defined directory structure and pushing it to a git repository. 
2. The second part is using this git repository to create a blueprint on Facets console.
[block:callout]
{
  "type": "info",
  "body": "The directory structure and directory names have to be in pre-defined format. Each resource type has a directory named after it. Each resource is defined by a JSON file, usually placed in the instances directory of a resource type. For example, if an application instance is needed, the directory name should be application and its json file would be placed in instances directory inside application directory"
}
[/block]
Our directory structure would look like below
[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/1f3eda9-Screen_Shot_2022-03-30_at_9.04.04_PM.png",
        "Screen Shot 2022-03-30 at 9.04.04 PM.png",
        480,
        520,
        "#1b1b1b"
      ]
    }
  ]
}
[/block]
Create <code>features.json</code> and <code>stack.json</code> as described in [Bootstrap a Blueprint](doc:deploy-our-first-app). 

1. <code>app.json</code> - Create a file named <code>app.json</code> at <code>application/instances</code> the following content in it. This file defines all the application resource related configurations like sizing, autoscaling, load balancing etc.
[block:code]
{
  "codes": [
    {
      "code": "{\n    \"$schema\": \"https://docs.facets.cloud/schemas/application/instances/application.schema\",\n     \"kind\": \"facets.modules.common.application\",\n     \"disabled\": false,\n     \"apiVersion\": \"v2\",\n     \"metadata\": {\n   \n     },\n     \"spec\": {\n   \n      \"env\": {\n   \n      },\n      \"loadbalancing\": {\n       \"rules\": [\n        {\n         \"ingress\": \"ingress\",\n         \"path\": \"/\",\n         \"portName\" : \"port5000\"\n        }\n       ]\n      },\n      \"permission\": [],\n      \"release\": {\n       \"strategy\": \"RollingUpdate\",\n       \"build\": {\n        \"image\": \"313496281593.dkr.ecr.us-east-1.amazonaws.com/facets/springbootsample/springboot-backend:latest\"\n       }\n      },\n      \"runtime\": {\n       \"size\": {\n        \"value\": \"small\",\n        \"namespace\": \"GP\"\n       },\n       \"autoscaling\": {\n        \"cpuThreshold\": \"50\",\n        \"max\": 1,\n        \"min\": 1\n       },\n       \"ports\": [{\n        \"name\": \"port5000\",\n        \"port\": 5000\n       }]\n      }\n\n",
      "language": "json"
    }
  ]
}
[/block]
2. <code>sizing.GP.json</code> - Create a file named <code>sizing.GP.json</code> inside the <code>application/</code> and put the following content in it. This file provides T-Shirt sizing options that can be used in app.json.
[block:code]
{
  "codes": [
    {
      "code": "{\n    \"$schema\": \"https://docs.facets.cloud/schemas/application/sizing.schema\",\n \n    \"small\": {\n      \"podCPULimit\": 1,\n      \"podMemoryLimit\": 2\n    }\n  }\n",
      "language": "json"
    }
  ]
}
[/block]
3. <code>ingress.json</code> - Create a directory named <code>ingress/instances</code>. Place <code>ingress.json</code> with the following content in it. This file defines all the ingress related resource configurations.
[block:code]
{
  "codes": [
    {
      "code": "{\n    \"$schema\": \"https://docs.facets.cloud/schemas/ingress/instances/ingress.schema\",\n    \"apiVersion\": \"v2\",\n    \"subdomains\" : []\n}\n",
      "language": "json"
    }
  ]
}
[/block]
#Step 2: Launch the App

1. Push all the files to a git repository and [register](doc:deploy-our-first-app#register-the-blueprint) the blueprint in the control plane
2. Push the built using :point-down: 
[block:tutorial-tile]
{
  "title": "Push a Build to Facets Control Plane Manually",
  "emoji": "✈️",
  "backgroundColor": "#802f9d",
  "slug": "push-a-build-to-facets-control-plane-manually",
  "_id": "638dcfb3e5003e002bee7e34",
  "id": "638dcfb3e5003e002bee7e34",
  "link": "https://facets.readme.io/v0.5/recipes/push-a-build-to-facets-control-plane-manually",
  "align": "center"
}
[/block]
3. [Launch](doc:launch-an-environment) an environment
4. Test the application using the steps [here](doc:deploy-a-service#test-the-application)