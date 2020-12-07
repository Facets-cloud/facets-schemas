package com.capillary.ops.cp.service.notification;


import com.capillary.ops.cp.bo.notifications.ChannelType;
import com.capillary.ops.cp.bo.notifications.Notification;
import com.capillary.ops.cp.bo.notifications.Subscription;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Service
public class FlockNotifier implements Notifier {

    private static class Message {

        public Message(String text) {
            this.text = text;
        }

        public Message() {
        }

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @Override
    public void notify(Subscription subscription, Notification notification) {
        RestTemplate restTemplate = new RestTemplate();
        Message message = new Message(notification.getNotificationMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> httpEntity = new HttpEntity<>(message, headers);
        restTemplate.postForEntity(subscription.getChannelAddress(), httpEntity, Object.class);
    }

    @Override
    public ChannelType getChannelType() {
        return ChannelType.FLOCK;
    }

    public void notify(String webHookUrl, String message) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        Message payload = new Message(message);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Message> httpEntity = new HttpEntity<>(payload, httpHeaders);
        restTemplate.postForEntity(webHookUrl, httpEntity, Object.class);
    }
}
