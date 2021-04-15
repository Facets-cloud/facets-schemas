package com.capillary.ops.cp.service.notification;


import com.capillary.ops.cp.bo.Team;
import com.capillary.ops.cp.bo.notifications.ChannelType;
import com.capillary.ops.cp.bo.notifications.Notification;
import com.capillary.ops.cp.bo.notifications.NotificationTag;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.repository.SubscriptionRepository;
import com.capillary.ops.cp.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private List<Notifier> notifiers;

    @Autowired
    private TeamRepository teamRepository;

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

        List<Subscription> blanketSubscriptions = subscriptionRepository.findByStackNameAndNotificationTypeAndNotificationSubject(
                notification.getStackName(), notification.getNotificationType(),
                Subscription.ALL);
        subscriptions.addAll(blanketSubscriptions);
        subscriptions.stream()
                .filter(x -> {
                    for (NotificationTag t : x.getFilters().keySet()) {
                        String v = notification.getNotificationTags().get(t);
                        if (x.getFilters().get(t).stream().noneMatch(f -> f.equalsIgnoreCase(v))) {
                            return false;
                        }
                    }
                    return true;
                })
                .forEach(x -> {
                    try {
                        notifierMap.get(x.getChannelType()).notify(x.getChannelAddress(), notification);
                    } catch (Throwable t) {
                        // pass
                    }
                });

        // Notify team channels
        if (notification.getTeamResource() != null) {
            List<Team> teams = teamRepository.findAll()
                    .stream().filter(x -> x.getResources().contains(notification.getTeamResource()))
                    .collect(Collectors.toList());
            for (Team team : teams) {
                Map<ChannelType, String> notificationChannels = team.getNotificationChannels();
                Set<ChannelType> channelTypes = notificationChannels.keySet();
                channelTypes.forEach(x -> notifierMap.get(x).notify(notificationChannels.get(x), notification));
            }
        }

    }

    public List<Subscription> getAllSubscriptions(String stackName) {
        return subscriptionRepository.findByStackName(stackName);
    }

    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }
}
