package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AppDeployment;
import com.capillary.ops.cp.bo.TeamResource;
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
        /**
         * https://app.slack.com/block-kit-builder/T01RKJ2FY3H#%7B%22blocks%22:%5B%7B%22type%22:%22header%22,%22text%22:%7B%22type%22:%22plain_text%22,%22text%22:%22Application%20Deloyment%22,%22emoji%22:true%7D%7D,%7B%22type%22:%22section%22,%22fields%22:%5B%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Stack:*%5Cncapillary-cloud%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Cluster:*%5Cnroot%22%7D%5D%7D,%7B%22type%22:%22section%22,%22fields%22:%5B%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Application:*%5Cn%20control-plane%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Release%20Type:*%5Cn%20HOTFIX%20%22%7D%5D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Artifact:*%5Cn313496281593.dkr.ecr.us-east-1.amazonaws.com/ops/deployer:cbd4a97-1273%22%7D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22%3Chttps://root.console.facets.cloud/capc/capillary-cloud/cluster/6021170c9c2b8600066a11f3/releases%7CView%20releases%3E%22%7D%7D,%7B%22type%22:%22divider%22%7D%5D%7D
         */
        String template = "{\"blocks\":[{\"type\":\"header\",\"text\":{\"type\":\"plain_text\",\"text\":\"Application" +
                " Deloyment\",\"emoji\":true}},{\"type\":\"section\",\"fields\":[{\"type\":\"mrkdwn\"," +
                "\"text\":\"*Stack:*\\n%s\"},{\"type\":\"mrkdwn\",\"text\":\"*Cluster:*\\n%s\"}]}," +
                "{\"type\":\"section\",\"fields\":[{\"type\":\"mrkdwn\",\"text\":\"*Application:*\\n " +
                "%s\"},{\"type\":\"mrkdwn\",\"text\":\"*Release Type:*\\n %s \"}]}," +
                "{\"type\":\"section\",\"text\":{\"type\":\"mrkdwn\",\"text\":\"*Artifact:*\\n%s\"}}," +
                "{\"type\":\"divider\"}]}";

        return String.format(template,cluster.getStackName(),cluster.getName(),
                appDeployment.getAppName(), appDeployment.getArtifact().getReleaseType(),
                appDeployment.getArtifact().getArtifactUri());
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

    @Override
    public TeamResource getTeamResource() {
      return new TeamResource(getStackName(), "application", getNotificationSubject());
    }
}
