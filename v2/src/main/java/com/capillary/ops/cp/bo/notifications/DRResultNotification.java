package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DRResult;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class DRResultNotification implements Notification {

    private DRResult callbackResult;

    private AbstractCluster cluster;

    public DRResultNotification(DRResult callbackResult, AbstractCluster cluster) {
        this.callbackResult = callbackResult;
        this.cluster = cluster;
    }

    public DRResultNotification() {
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.DR_RESULT;
    }

    @Override
    public String getNotificationMessage() {
        String subject = callbackResult.getResourceType() + "-" + callbackResult.getInstanceName();
        String message =  String.format("Performed action: %s for module: %s in cluster: %s of stack: %s | status: %s",
                callbackResult.getAction(), subject, cluster.getName(), cluster.getStackName(),
                callbackResult.getStatus());
        if (DRResult.DRStatus.FAILURE.equals(callbackResult.getStatus())) {
            message += " | exception: "  + callbackResult.getException();
        }
        return message;
    }

    @Override
    public Map<NotificationTag, String> getNotificationTags() {
        return ImmutableMap.of(
                NotificationTag.CLUSTER_NAME, cluster.getName(),
                NotificationTag.CLUSTER_TYPE, cluster.getReleaseStream().name(),
                NotificationTag.DR_ACTION, callbackResult.getAction().name(),
                NotificationTag.DR_STATUS, callbackResult.getStatus().name()
        );
    }

    @Override
    public String getNotificationSubject() {
        return callbackResult.getResourceType();
    }

    @Override
    public String getStackName() {
        return cluster.getStackName();
    }
}
