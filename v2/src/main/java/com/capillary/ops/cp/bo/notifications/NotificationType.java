package com.capillary.ops.cp.bo.notifications;

public enum NotificationType {
  APP_DEPLOYMENT(new NotificationTag[]{NotificationTag.CLUSTER_NAME, NotificationTag.CLUSTER_TYPE});
  private NotificationTag[] supportedTags;

  NotificationType(NotificationTag[] tags) {
    supportedTags = tags;
  }

  NotificationType() {
  }
}
