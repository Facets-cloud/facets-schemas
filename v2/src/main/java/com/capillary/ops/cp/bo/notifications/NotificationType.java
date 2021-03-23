package com.capillary.ops.cp.bo.notifications;

public enum NotificationType {
  APP_DEPLOYMENT(new NotificationTag[]{NotificationTag.CLUSTER_NAME, NotificationTag.CLUSTER_TYPE}),
  QASUITE_SANITY(new NotificationTag[]{NotificationTag.CLUSTER_NAME, NotificationTag.CLUSTER_TYPE, NotificationTag.QASUITE_RESULT}),
  DR_RESULT(new NotificationTag[]{NotificationTag.CLUSTER_NAME, NotificationTag.CLUSTER_TYPE, NotificationTag.DR_ACTION, NotificationTag.DR_STATUS}),
  STACK_SIGNOFF(new NotificationTag[]{}),
  ALERT(new NotificationTag[]{NotificationTag.CLUSTER_NAME,NotificationTag.CLUSTER_TYPE, NotificationTag.SEVERITY});
  private NotificationTag[] supportedTags;

  NotificationType(NotificationTag[] tags) {
    supportedTags = tags;
  }

  NotificationType() {
  }
}
