package com.capillary.ops.cp.service.notification;

import com.capillary.ops.cp.bo.notifications.ChannelType;
import com.capillary.ops.cp.bo.notifications.Notification;
import com.capillary.ops.cp.bo.notifications.Subscription;

public interface Notifier {
    void notify(String channelAddress, Notification notification);
    ChannelType getChannelType();
}
