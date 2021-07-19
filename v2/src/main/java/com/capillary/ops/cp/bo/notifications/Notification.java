package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.TeamResource;

import java.util.Map;

public interface Notification {
  NotificationType getNotificationType();
  String getNotificationMessage();
  Map<NotificationTag, String> getNotificationTags();
  String getNotificationSubject();
  String getStackName();
  default Boolean isPayloadJson(){ return false;}
  default TeamResource getTeamResource() { return null; };
}
