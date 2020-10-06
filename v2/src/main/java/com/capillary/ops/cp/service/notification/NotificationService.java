package com.capillary.ops.cp.service.notification;


import com.capillary.ops.cp.bo.notifications.ChannelType;
import com.capillary.ops.cp.bo.notifications.Notification;
import com.capillary.ops.cp.bo.notifications.NotificationTag;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private List<Notifier> notifiers;


    private Map<ChannelType, Notifier> notifierMap = new HashMap<>();

    @PostConstruct
    public void init() {
        notifiers.stream().forEach(x -> notifierMap.put(x.getChannelType(), x));
    }

    public void publish(Notification notification) {
        List<Subscription> subscriptions =
                subscriptionRepository.findByStackNameAndNotificationTypeAndNotificationSubject(
                        notification.getStackName(), notification.getNotificationType(),
                        notification.getNotificationSubject());
        subscriptions.stream()
                .filter(x -> {
                    for (NotificationTag t: x.getFilters().keySet()) {
                        String v = notification.getNotificationTags().get(t);
                        if (! x.getFilters().get(t).stream().anyMatch(f -> f.equalsIgnoreCase(v))) {
                            return false;
                        }
                    }
                    return true;
                })
                .forEach(x -> {
                    try {
                        notifierMap.get(x.getChannelType()).notify(x, notification);
                    } catch (Throwable t) {
                        // pass
                    }
        });

    }

    public List<Subscription> getAllSubscriptions(String stackName) {
        return subscriptionRepository.findByStackName(stackName);
    }

    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }
}
