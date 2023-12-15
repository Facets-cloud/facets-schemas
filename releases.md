---
title: "Performing a Release"
slug: "releases"
excerpt: "How to perform releases and gather information about a particular release"
hidden: true
createdAt: "2022-03-25T08:50:40.798Z"
updatedAt: "2023-02-14T20:10:10.401Z"
---
<<glossary:Releases>> are mutations that propagates to an environment. In this Section, you will learn how to perform and monitor a release that makes a change to the environment. 

Facets is built on a key principle that any changes (release) to an environment has to happen through a single mechanism. These changes could be of

1. New code or updates (new builds) 
2. New resources or updates
3. New interactions, schema, policies or updates

# Release Schedule

Releases are of two types

1. Scheduled Release: A Scheduled release [pattern](https://crontab.guru/) is defined in the "edit" environment section in the "Misc" tab. For e.g., a <code>30 5 \* \* \*</code> would mean a scheduled release (full) will happen at <code>5:30 AM, every day</code>.
2. On-demand Release: An on-demand release can be performed in an environment from the "Releases" page. You can make a full or partial release from this page.  

   1. Release - Perform a full release i.e. every resource on the Blueprint and their qualified builds will sync to the environment
   2. Selective - Perform a selective release of one or selected resources. This is usually used as a hotfix to the environment

![](https://files.readme.io/ca75549-Screen_Shot_2022-03-29_at_10.18.29_PM.png "Screen Shot 2022-03-29 at 10.18.29 PM.png")



# Release Candidates

Once a release is performed the following details are available for it. 

[block:parameters]
{
  "data": {
    "h-0": "",
    "h-1": "",
    "0-0": "Started",
    "0-1": "When was the release triggered",
    "1-0": "Status",
    "1-1": "<span style=\"color:green\">SUCCESS</span> or <span style=\"color:red\">FAIL</span>",
    "2-0": "Last Commit",
    "2-1": "The git commit up to which the IDP has pulled the changes",
    "3-0": "Release Type",
    "3-1": "RELEASE (Indicates a full release)  \nLAUNCH (Environment launch)  \nHOTFIX (Indicates a selective release)",
    "4-0": "Triggered By",
    "4-1": "Either a **user**  \nOr  \n**Facets Bot** (indicating a scheduled release)",
    "5-0": "Action",
    "5-1": "1. Release Details  \n2. Terraform Logs"
  },
  "cols": 2,
  "rows": 6,
  "align": [
    "left",
    "left"
  ]
}
[/block]

## Release Details

The release details Action shows the resource changes i.e <span style="color:green">Creations</span>, <span style="color:GoldenRod">Modification</span> (i.e. new versions of apps) or <span style="color:red">Destruction</span>.

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/7169c77-Screen_Shot_2022-03-29_at_11.22.57_PM.png",
        "Screen Shot 2022-03-29 at 11.22.57 PM.png",
        2024
      ],
      "align": "center",
      "caption": "Release Details"
    }
  ]
}
[/block]

## Release Logs

The release logs provide a detail view of the generated terraform logs. 

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/4c2ff6b-Screen_Shot_2022-03-30_at_12.01.10_AM.png",
        "Screen Shot 2022-03-30 at 12.01.10 AM.png",
        2390
      ],
      "align": "center",
      "caption": "Terraform Logs"
    }
  ]
}
[/block]

# Release Summary

The release summary page is aggregated release summary for last 30 days. This provides metrics that can lead to improvement in the SDLC. 

|                      |                                                  |
| :------------------- | :----------------------------------------------- |
| Successful Releases  | Number of Successful Releases in last 30 days    |
| Failed Releases      | Number of Failed Releases in last 30 days        |
| Deployment Frequency | Average deployment per day                       |
| Change Failure Rate  | Percentage of changes that resulted in a failure |