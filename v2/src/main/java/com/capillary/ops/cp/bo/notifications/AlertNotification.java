package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AlertManagerPayload;
import com.capillary.ops.cp.bo.TeamResource;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AlertNotification implements Notification {

    private AbstractCluster cluster;
    private AlertManagerPayload.Alert alert;

    List<String> labelIgnoreList = Arrays.asList("severity", "job","service", "cluster","endpoint", "namespace",
            "prometheus","team");



    public AlertNotification(AbstractCluster cluster, AlertManagerPayload.Alert alert) {
        this.cluster = cluster;
        this.alert = alert;
    }


    @Override
    public NotificationType getNotificationType() {
        return NotificationType.ALERT;
    }

    @Override
    /**
     * https://app.slack.com/block-kit-builder/T01RKJ2FY3H#%7B%22blocks%22:%5B%7B%22type%22:%22header%22,%22text%22:%7B%22type%22:%22plain_text%22,%22text%22:%22Warning%20Alert%22,%22emoji%22:true%7D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Deployment%20has%20non-ready%20pods%20for%20longer%20than%20a%2010m.*%22%7D%7D,%7B%22type%22:%22section%22,%22fields%22:%5B%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Stack:*%5Cncapillary-cloud%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Cluster:*%5Cnroot%22%7D%5D%7D,%7B%22type%22:%22divider%22%7D,%7B%22type%22:%22section%22,%22fields%22:%5B%7B%22type%22:%22mrkdwn%22,%22text%22:%22*alertname:*%5Cn%20CCDeploymentNonReadyPodsdeployment%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*deployment:*%5Cn%20lms%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*instance:*%5Cn%2010.161.36.18:8080%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*pod:*%5Cn%20prometheus-operator-kube-state-metrics-7f979567df-nrzld%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*resourceType:*%5Cn%20application%22%7D,%7B%22type%22:%22mrkdwn%22,%22text%22:%22*resourceName:*%5Cn%20lms%22%7D%5D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22%20%22%7D,%22accessory%22:%7B%22type%22:%22button%22,%22text%22:%7B%22type%22:%22plain_text%22,%22text%22:%22View%20Alerts%22,%22emoji%22:true%7D,%22value%22:%22click_me_123%22,%22url%22:%22https://vymo-facets-cp.console.facets.cloud/capc/vymo-default/cluster/60cef9a1f71ee900076259ec/alerts%22,%22action_id%22:%22button-action%22%7D%7D,%7B%22type%22:%22divider%22%7D%5D%7D
     */
    public String getNotificationMessage() {
        String raw= "{'blocks':[{'type':'header','text':{'type':'plain_text','text':'%s Alert'," +
                "'emoji':true}},{'type':'section','text':{'type':'mrkdwn','text':'*%s* - %s'}},{'type':'section'," +
                "'fields':[{'type':'mrkdwn'," +
                "'text':'*Stack:*\\n%s'},{'type':'mrkdwn','text':'*Cluster:*\\n%s'}]}," +
                "{'type':'divider'}]}";
        raw = String.format(raw, StringUtils.capitalize(alert.getSeverity()), alert.getMessage(), alert.getStatus(),
                cluster.getStackName(),
                cluster.getName());
        JSONObject json = new JSONObject(raw);
        JSONArray blocks = json.getJSONArray("blocks");
        JSONObject labels = new JSONObject();
        JSONArray fields = new JSONArray();
        labels.put("type","section");
        for(Map.Entry label : alert.getLabels().entrySet()){
            if(!labelIgnoreList.contains(label.getKey())){
                JSONObject labelObj = new JSONObject();
                labelObj.put("type","mrkdwn");
                labelObj.put("text", "*" + label.getKey() + "*\n"+label.getValue());
                fields.put(labelObj);
            }
        }
        if(fields.length()>0) {
            labels.put("fields", fields);
            blocks.put(labels);
            blocks.put(new JSONObject("{'type':'divider'}"));
        }
        return json.toString();
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

    @Override
    public Boolean isPayloadJson(){
        return true;
    }
}
