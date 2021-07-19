package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AppDeployment;
import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.notifications.ApplicationDeploymentNotification;
import com.capillary.ops.cp.bo.notifications.NotificationChannel;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.bo.requests.Cloud;
import com.capillary.ops.cp.repository.NotificationChannelRepository;
import com.capillary.ops.cp.repository.SubscriptionRepository;
import com.capillary.ops.cp.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionFacade {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public void deleteSubscription(String subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }


    public Subscription editSubscription(Subscription subscription) {
        if(!subscriptionRepository.findById(subscription.getId()).isPresent()){
            throw new RuntimeException("Subscription with Id "+ subscription.getId() + "is not present");
        }
        Subscription ret = subscriptionRepository.save(subscription);
        return ret;
    }


    public List<Subscription> getAllSubscriptions(String stackName) {
        return notificationService.getAllSubscriptions(stackName);
    }

    public List<Subscription> getAllSubscriptions() {
        return notificationService.getAllSubscriptions();
    }

    public Subscription createSubscription(Subscription subscription) {
        Subscription ret = notificationService.createSubscription(subscription);
        return ret;
    }
}
