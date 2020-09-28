package com.capillary.ops.cp.bo.notifications;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
public class Subscription {
  @Id
  private String id;

  // Eg. FLOCK, EMAIL
  private ChannelType channelType;

  // Eg. flock webhook endpoint, email address
  private String channelAddress;

  // Eg BUILD, DEPLOYMENT, BUILD_PROMOTION
  private EventType eventType;

  private Map<String, String> eventScopes;

  private String eventPrincipal;

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

  public EventType getEventType() {
    return eventType;
  }

  public void setEventType(EventType eventType) {
    this.eventType = eventType;
  }

  public Map<String, String> getEventScopes() {
    return eventScopes;
  }

  public void setEventScopes(Map<String, String> eventScopes) {
    this.eventScopes = eventScopes;
  }

  public String getEventPrincipal() {
    return eventPrincipal;
  }

  public void setEventPrincipal(String eventPrincipal) {
    this.eventPrincipal = eventPrincipal;
  }
}
