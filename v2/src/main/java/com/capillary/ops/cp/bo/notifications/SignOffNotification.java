package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.DeploymentLog;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.VCS;

import java.util.HashMap;
import java.util.Map;

public class SignOffNotification implements Notification {
    private DeploymentLog deploymentLog;
    private AbstractCluster cluster;
    private DeploymentLog lastSignedOff;
    private Stack stack;

    public SignOffNotification() {
    }

    public SignOffNotification(Stack stack, AbstractCluster cluster, DeploymentLog deploymentLog, DeploymentLog lastSignedOff) {
        this.deploymentLog = deploymentLog;
        this.cluster = cluster;
        this.lastSignedOff = lastSignedOff;
        this.stack = stack;
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.STACK_SIGNOFF;
    }

    @Override
    public String getNotificationMessage() {
        return "Version " + deploymentLog.getStackVersion().substring(0, 7) + " of stack "
                + this.cluster.getStackName() + " has been signed-off" + " from " + this.cluster.getName()
                + ". Diff: " + getDiffUrl();
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
        return this.cluster.getStackName();
    }

    private String getDiffUrl() {
        if (this.lastSignedOff == null) {
            return "NA";
        }
        if (this.stack.getVcs() == VCS.GITHUB) {
            return this.stack.getVcsUrl().replace(".git", "") + "/compare/"
                    + this.lastSignedOff.getStackVersion() + "..." + deploymentLog.getStackVersion();
        } else if (this.stack.getVcs() == VCS.BITBUCKET) {
            return this.stack.getVcsUrl().replace(".git", "") + "/compare/" +
                    deploymentLog.getStackVersion() + "%0" + this.lastSignedOff.getStackVersion();
        }

        return "NA";
    }

}
