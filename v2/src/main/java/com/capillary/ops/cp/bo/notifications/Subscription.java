package com.capillary.ops.cp.bo.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
public class Subscription {
  @Id
  private String id;

  private String stackName;

  // Eg. FLOCK, EMAIL
  private ChannelType channelType;

  // Eg. flock webhook endpoint, email address
  private String channelAddress;

  // Eg BUILD, DEPLOYMENT, BUILD_PROMOTION
  private NotificationType notificationType;

  private String notificationSubject;

  private Map<NotificationTag, List<String>> filters;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ChannelType getChannelType() {
    return channelType;
  }

  public void setChannelType(ChannelType channelType) {
    this.channelType = channelType;
  }

  public String getChannelAddress() {
    return channelAddress;
  }

  public void setChannelAddress(String channelAddress) {
    this.channelAddress = channelAddress;
  }

  public NotificationType getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(NotificationType notificationType) {
    this.notificationType = notificationType;
  }

  public String getStackName() {
    return stackName;
  }

  public void setStackName(String stackName) {
    this.stackName = stackName;
  }

  public String getNotificationSubject() {
    return notificationSubject;
  }

  public void setNotificationSubject(String notificationSubject) {
    this.notificationSubject = notificationSubject;
  }

  public Map<NotificationTag, List<String>> getFilters() {
    return filters;
  }

  public void setFilters(Map<NotificationTag, List<String>> filters) {
    this.filters = filters;
  }
}
