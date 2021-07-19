package com.capillary.ops.cp.service.notification;

import com.capillary.ops.cp.bo.notifications.ChannelType;
import com.capillary.ops.cp.bo.notifications.Notification;

import java.util.List;

public interface Notifier {
    void notify(String channelAddress, Notification notification);
    List<ChannelType> getChannelTypes();
}
