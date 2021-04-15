package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AlertManagerPayload;
import com.capillary.ops.cp.bo.TeamResource;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class AlertNotification implements Notification {

    private AbstractCluster cluster;
    private AlertManagerPayload.Alert alert;


    public AlertNotification(AbstractCluster cluster, AlertManagerPayload.Alert alert) {
        this.cluster = cluster;
        this.alert = alert;
    }


    @Override
    public NotificationType getNotificationType() {
        return NotificationType.ALERT;
    }

    @Override
    public String getNotificationMessage() {
        return "[" + alert.getSeverity() + "] [" + cluster.getStackName() + "/" + cluster.getName() +
                "/" + alert.getResourceType() + "/" + alert.getResourceName() + "] " + alert.getMessage();
    }

    @Override
    public Map<NotificationTag, String> getNotificationTags() {
        return ImmutableMap.of(
                NotificationTag.CLUSTER_NAME, cluster.getName(),
                NotificationTag.CLUSTER_TYPE, cluster.getReleaseStream().name(),
                NotificationTag.SEVERITY, alert.getSeverity()
        );
    }

    @Override
    public String getNotificationSubject() {
        return alert.getResourceName();
    }

    @Override
    public String getStackName() {
        return cluster.getStackName();
    }

    @Override
    public TeamResource getTeamResource() {
        return new TeamResource(getStackName(), alert.getResourceType(), getNotificationSubject());
    }
}
