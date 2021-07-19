package com.capillary.ops.cp.bo.notifications;

import java.util.HashMap;
import java.util.Map;

public class TestNotification implements Notification {
    @Override
    public NotificationType getNotificationType() {
        return NotificationType.ALERT;
    }

    @Override
    public String getNotificationMessage() {
        return "{\"blocks\":[{\"type\":\"header\",\"text\":{\"type\":\"plain_text\",\"text\":\"Test Notification\"," +
                "\"emoji\":true}},{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"This is a test " +
                "*notification* from your *Facets* control plane\"}}]}";
    }

    @Override
    public Map<NotificationTag, String> getNotificationTags() {
        return new HashMap<>();
    }

    @Override
    public String getNotificationSubject() {
        return "Test";
    }

    @Override
    public String getStackName() {
        return "Test";
    }

    @Override
    public Boolean isPayloadJson(){
        return true;
    }
}
