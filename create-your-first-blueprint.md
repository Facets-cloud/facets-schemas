---
title: "Create your first Blueprint"
slug: "create-your-first-blueprint"
hidden: false
metadata: 
  title: "Bootstrap a Blueprint - Facets DevOps Platform"
  description: "How to prepare an environment and deploy a spring boot application on Facets DevOps Platform"
createdAt: "2022-03-09T16:35:57.774Z"
updatedAt: "2023-06-15T11:51:45.138Z"
---
Once you have logged into your control plane, you will be presented with a screen as shown below:

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/f9eda02-image.png",
        null,
        "Create Blueprint Screen"
      ],
      "align": "center",
      "sizing": "650px",
      "border": true,
      "caption": "Create Blueprint Screen"
    }
  ]
}
[/block]

The only action available at this time is to create a Blueprint. On clicking on this button the following screen will open:

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/04b4d98-Untitled.gif",
        null,
        "Create Blueprint Form"
      ],
      "align": "center",
      "sizing": "650px",
      "border": true,
      "caption": "Create Blueprint Form"
    }
  ]
}
[/block]

1. **Provide a Unique Name:** Start by giving your Blueprint a unique name.
2. **Select Creation Type:** Choose whether you want to create a new Blueprint or import an existing one. 
3. **Choose Blueprint Template:** You can opt to launch an empty Blueprint or select from a variety of sample Blueprint templates.  
   Please note that the **Blueprint Template** field will only be displayed when you opt to create a new Blueprint.
4. **Link Your Version Control System (VCS) Account:** In this step, ensure you have an empty repository in your preferred VCS platform (e.g., GitHub, Bitbucket, GitLab). Enter the **Repository URL** and follow the on-screen instructions or refer to the provided [guide](https://readme.facets.cloud/docs/integrating-vcs-accounts) for linking your Bitbucket, GitHub, or GitLab account with Facets. 
5. **Configure Optional Fields: **Customize your Blueprint by specifying the master **Branch Name** (e.g., "main" in GitHub or "master" in Bitbucket), defining the **Relative Path** for Blueprint initialization (if applicable), and choosing your **Primary Cloud** provider. 
6. **Submit Your Blueprint:** Once you've completed the necessary steps, click the **Create** button to initiate the creation of your Blueprint. For a better understanding of Blueprint concepts and functionality, we recommend reviewing the [Blueprint Concepts](doc:blueprint) documentation. 

Congratulations! You've successfully created your first Blueprint in Facets.