---
title: "Creating Notification Channels and Subscriptions"
slug: "notification-channels-subscriptions"
hidden: false
createdAt: "2022-09-01T04:41:39.024Z"
updatedAt: "2023-07-21T13:46:27.096Z"
---
Facets Control Plane provides the capability to create notification channels and subscriptions for receiving notifications.  
This document walks you through the process of creating Notification Channels and Subscriptions.

Navigate to **Settings > Notifications** to access the Notifications section.

## Creating a Notification Channel

1. Select the **Channels** tab.  
   You'll find all the Notification Channels created within the Facets Control Plane.
2. Click **Create Channel.**
3. In the UI that appears, select the **Channel Type** and **Channel Name.**
4. Choose the **Channel Type.**
   1. For **Slack, Webhook, Zen Duty, or Microsoft Teams: **Mention the **Channel URL** (URL for the Webhook where you want to receive notifications).  
      **Note: **For Microsoft Teams, follow [this](https://learn.microsoft.com/en-us/microsoftteams/platform/webhooks-and-connectors/how-to/add-incoming-webhook?tabs=dotnet#create-incoming-webhooks-1) guide to learn how to get the channel URL.
   2. For **Pager Duty:** Mention the **Channel URL **and** Integration Key.**
   3. For **Email: **Select the email addresses of **Users** within your Control Plane and/or specify **Custom Email IDs** for users who are not part of the Control Plane.
5. Click **Test Notifications** to test the notifications.
6. Click **Create.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/ccdaa95-Notification_channel.gif",
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

You have successfully created a Notification Channel.

## Creating Subscriptions

1. Select the **Subscriptions** tab.  
   You'll find all the Subscriptions created within the Facets Control Plane.
2. Click **Create Subscription.**
3. In the UI that appears, mention the **Subscription Name, Notification Type, Blueprint, Channel Type, **and** Channel Name.**
4. Based on the **Notification Type,** you will be able to add **Filters** such as **Alert Name, Environment Name, Release Stream, **and** Severity.**
5. Click **Test Notifications** to test the notifications for the subscription.
6. Click **Create.**

[block:image]
{
  "images": [
    {
      "image": [
        "https://files.readme.io/4a67f36-notification_subscription.gif",
        null,
        "Create Notification Subscription"
      ],
      "align": "center",
      "sizing": "450px",
      "border": true,
      "caption": "Create Notification Subscription"
    }
  ]
}
[/block]

You have successfully created a Subscription.