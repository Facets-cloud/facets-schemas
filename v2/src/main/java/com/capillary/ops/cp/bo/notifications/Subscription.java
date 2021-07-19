package com.capillary.ops.cp.bo.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@Getter
@Setter
@NoArgsConstructor
public class Subscription {
    public static final String ALL = "*";
    @Id
    private String id;

    private String stackName;

    @Deprecated
    private ChannelType channelType;

    @Deprecated
    private String channelAddress;

    // Eg BUILD, DEPLOYMENT, BUILD_PROMOTION
    private NotificationType notificationType;

    private String channelId;

    private String notificationSubject = ALL;

    private Map<NotificationTag, List<String>> filters = new HashMap<>();

}
