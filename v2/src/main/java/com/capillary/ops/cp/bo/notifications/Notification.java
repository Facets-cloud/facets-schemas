package com.capillary.ops.cp.bo.notifications;

import java.util.Map;

public interface Notification {
  NotificationType getNotificationType();
  String getNotificationMessage();
  Map<NotificationTag, String> getNotificationTags();
  String getNotificationSubject();
  String getStackName();
}
