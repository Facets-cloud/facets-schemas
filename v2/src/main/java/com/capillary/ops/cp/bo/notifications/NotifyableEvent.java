package com.capillary.ops.cp.bo.notifications;

import java.util.List;

public interface NotifyableEvent {
  EventType getNotificationEventType();
  String getNotificationEventMessage();
  List<String> getNotificationEventScopes();
  String getNotificationEventPrincipal();
}
