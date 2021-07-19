package com.capillary.ops.cp.controller.ui;

import com.capillary.ops.cp.bo.notifications.ChannelType;
import com.capillary.ops.cp.bo.notifications.NotificationChannel;
import com.capillary.ops.cp.bo.notifications.NotificationType;
import com.capillary.ops.cp.bo.notifications.Subscription;
import com.capillary.ops.cp.facade.SubscriptionFacade;
import com.capillary.ops.cp.repository.NotificationChannelRepository;
import com.capillary.ops.cp.service.notification.NotificationService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("cc-ui/v1/notification")
@Loggable()
public class UiNotificationController {

    @Autowired
    private SubscriptionFacade subscriptionFacade;
    @Autowired
    private NotificationChannelRepository notificationChannelRepository;
    @Autowired
    private NotificationService notificationService;


    @GetMapping("/subscriptions")
    public List<Subscription> getAllSubscriptions() {
        return subscriptionFacade.getAllSubscriptions();
    }

    @GetMapping("/channels")
    public List<NotificationChannel> getAllChannels() {
        return notificationChannelRepository.findAll();
    }

    @GetMapping("/channelTypes")
    public List<ChannelType> getAllChannelTypes() {
        return Arrays.asList(ChannelType.values());
    }

    @GetMapping("/notificationTypes")
    public List<NotificationType> getAllNotificationTypes() {
        return Arrays.asList(NotificationType.values());
    }

    @PostMapping("/subscriptions")
    public List<Subscription> createSubscription(@RequestBody Subscription subscription) {
        subscriptionFacade.createSubscription(subscription);
        return subscriptionFacade.getAllSubscriptions();
    }

    @PutMapping("/subscriptions/{subscriptionId}")
    public List<Subscription> editSubscription(@PathVariable String subscriptionId,
                                               @RequestBody Subscription subscription) {
        subscription.setId(subscriptionId);
        subscriptionFacade.editSubscription(subscription);
        return subscriptionFacade.getAllSubscriptions();
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public List<Subscription> deleteSubscription(@PathVariable String subscriptionId) {
        subscriptionFacade.deleteSubscription(subscriptionId);
        return subscriptionFacade.getAllSubscriptions();
    }

    @PostMapping("/channels")
    public List<NotificationChannel> createNotificationChannel(@RequestBody NotificationChannel nc) {
        if (notificationChannelRepository.findByName(nc.getName()).isPresent()) {
            throw new RuntimeException("Channel with Same Name - " + nc.getName() + " already exists");
        }
        notificationChannelRepository.save(nc);
        return notificationChannelRepository.findAll();
    }
    @PutMapping("/channels/{channelId}")
    public List<NotificationChannel> editNotificationChannel(@PathVariable String channelId,
                                                             @RequestBody NotificationChannel nc) {
        if (!notificationChannelRepository.findById(channelId).isPresent()) {
            throw new RuntimeException("Channel with Id - " + channelId + " does not exists");
        }
        nc.setId(channelId);
        notificationChannelRepository.save(nc);
        return notificationChannelRepository.findAll();
    }
    @DeleteMapping("/channels/{channelId}")
    public List<NotificationChannel> deleteNotificationChannel(@PathVariable String channelId) {
        if (!notificationChannelRepository.findById(channelId).isPresent()) {
            throw new RuntimeException("Channel with Id - " + channelId + " does not exists");
        }
        notificationChannelRepository.deleteById(channelId);
        return notificationChannelRepository.findAll();
    }

    @PostMapping("/channels/test")
    public Boolean testNotificationChannel(@RequestBody NotificationChannel nc) {
        return notificationService.sendTestNotification(nc);
    }
}
