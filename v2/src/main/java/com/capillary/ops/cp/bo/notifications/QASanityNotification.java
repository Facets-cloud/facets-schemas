package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AppDeployment;
import com.capillary.ops.cp.bo.QASuiteModuleResult;
import com.capillary.ops.cp.bo.QASuiteResult;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class QASanityNotification implements Notification {

    private QASuiteModuleResult moduleResult;

    private AbstractCluster cluster;

    public QASanityNotification(QASuiteModuleResult moduleResult, AbstractCluster cluster) {
        this.moduleResult = moduleResult;
        this.cluster = cluster;
    }

    public QASanityNotification() {
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.QASUITE_SANITY;
    }

    @Override
    public String getNotificationMessage() {
        return String.format("Error while running sanity for module %s in cluster %s of stack %s, status: %s",
                moduleResult.getModuleName(), cluster.getName(), cluster.getStackName(), moduleResult.getStatus());
    }

    @Override
    public Map<NotificationTag, String> getNotificationTags() {
        return ImmutableMap.of(
                NotificationTag.CLUSTER_NAME, cluster.getName(),
                NotificationTag.CLUSTER_TYPE, cluster.getReleaseStream().name(),
                NotificationTag.QASUITE_RESULT, moduleResult.getStatus().name()
        );
    }

    @Override
    public String getNotificationSubject() {
        return moduleResult.getModuleName();
    }

    @Override
    public String getStackName() {
        return cluster.getStackName();
    }
}
