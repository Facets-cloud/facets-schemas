package com.capillary.ops.cp.bo.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document
public class NotificationChannel {

    @Id
    private String id;

    @Indexed(name = "name_index", unique = true)
    private String name;

    private String channelAddress;

    private ChannelType channelType;
}
