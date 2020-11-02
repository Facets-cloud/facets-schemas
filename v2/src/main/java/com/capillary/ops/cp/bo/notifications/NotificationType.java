package com.capillary.ops.cp.bo.notifications;

public enum NotificationType {
  APP_DEPLOYMENT(new NotificationTag[]{NotificationTag.CLUSTER_NAME, NotificationTag.CLUSTER_TYPE}),
  QASUITE_SANITY(new NotificationTag[]{NotificationTag.CLUSTER_NAME, NotificationTag.CLUSTER_TYPE, NotificationTag.QASUITE_RESULT});

  private NotificationTag[] supportedTags;

  NotificationType(NotificationTag[] tags) {
    supportedTags = tags;
  }

  NotificationType() {
  }
}
