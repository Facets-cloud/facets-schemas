---
title: "Notifications"
slug: "notifications"
hidden: false
createdAt: "2023-02-21T08:37:35.980Z"
updatedAt: "2023-03-03T10:01:50.939Z"
---
## Introduction

Staying on top of potential issues in complex software systems spanning multiple technologies requires receiving granular notifications for system events or deployments across environments. Facets offers a solution to this problem with its robust Notifications feature.

With Facets' Notifications, users can subscribe to notifications for different events and receive them through various channels from their Control Plane. All notifications in Facets are based on a subscription model, which means that a user can subscribe to any unique grouping of Blueprint, Channel, and Notification Type.

## Notification Channels

Notification Channels are the webhooks through which notification are distributed to the user. Currently, Facets offers Zenduty, Pagerduty, Slack integrations out of the box. Users can also choose to add custom webhooks and payload to receive notifications.

## Subscriptions

Subscriptions are a logical expression of the conditions for when a user wants to receive a notification. All subscriptions are attached to a blueprint and channel, and can be categorised under multiple notification types. This level of granularity allows users to receive the information they need, without being inundated with irrelevant alerts.

### Advanced Filters

To further customize notifications, users can set advanced filters for their subscriptions. Advanced filters can further fine tune the subscription based on notification types. For example, a subscription can be created only for alerts of 'Critical' severity in a particular blueprint. 

In conclusion, the Notifications feature in Facets provides users with an effective way to stay informed about events and deployments across their systems, with the flexibility to customize notifications to their specific needs.

## FAQs

### What are the different notification types in Facets?

Facets currently supports the following notification types: 

- Alerts
- Application Deployment In Progress and Complete status
- Deployment Status updates
- Environment Configuration, Create, Delete, Destroy and Launch actions
- Stack Signoff

### What are the different Channel Types supported in Facets?

Facets currently supports the following channel types:

- PagerDuty
- Zenduty
- Slack
- Generic Webhooks

### Can I test notifications from the Control Plane?

Yes, Facets provides users with a Test Notification button to receive a mock notification based on their subscription. Using this, users can mimic how an actual notification will look like from their subscription.

## Related Guides

- [Creating Notification Channels and Subscriptions](doc:notification-channels-subscriptions)