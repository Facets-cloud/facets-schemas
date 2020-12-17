package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.DeploymentLog;

import java.util.HashMap;
import java.util.Map;

public class SignOffNotification implements Notification {
    private DeploymentLog deploymentLog;
    private String stackName;
    private String clusterName;

    public SignOffNotification() {
    }

    public SignOffNotification(String stackName, String clusterName, DeploymentLog deploymentLog) {
        this.deploymentLog = deploymentLog;
        this.stackName = stackName;
        this.clusterName = clusterName;
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.STACK_SIGNOFF;
    }

    @Override
    public String getNotificationMessage() {
        return "Version " + deploymentLog.getStackVersion().substring(0, 7) + " of stack "
                + this.stackName + " has been signed-off" + " from " + this.clusterName;
    }

    @Override
    public Map<NotificationTag, String> getNotificationTags() {
        return new HashMap<>();
    }

    @Override
    public String getNotificationSubject() {
        return deploymentLog.getClusterId();
    }

    @Override
    public String getStackName() {
        return stackName;
    }
}
