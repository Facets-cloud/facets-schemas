package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AppDeployment;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ApplicationDeploymentNotification implements Notification {

    private AppDeployment appDeployment;

    private AbstractCluster cluster;

    public ApplicationDeploymentNotification(AppDeployment appDeployment, AbstractCluster cluster) {
        this.appDeployment = appDeployment;
        this.cluster = cluster;
    }

    public ApplicationDeploymentNotification() {
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.APP_DEPLOYMENT;
    }

    @Override
    public String getNotificationMessage() {
        return String.format("%s deployed in cluster %s of stack %s. Artifact deployed: %s (Build Id: %s)",
                appDeployment.getAppName(), cluster.getName(), cluster.getStackName(),
                appDeployment.getArtifact().getArtifactUri(), appDeployment.getArtifact().getBuildId());
    }

    @Override
    public Map<NotificationTag, String> getNotificationTags() {
        return ImmutableMap.of(
                NotificationTag.CLUSTER_NAME, cluster.getName(),
                NotificationTag.CLUSTER_TYPE, cluster.getReleaseStream().name()
        );
    }

    @Override
    public String getNotificationSubject() {
        return appDeployment.getAppName();
    }

    @Override
    public String getStackName() {
        return cluster.getStackName();
    }
}
