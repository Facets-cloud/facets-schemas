package com.capillary.ops.cp.bo.notifications;

import com.capillary.ops.cp.bo.AlertManagerPayload;
import com.capillary.ops.cp.bo.AwsCluster;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class AlertNotificationTest {

    @Test
    public void testNotification(){
        AwsCluster cluster = new AwsCluster("testCluster");
        cluster.setStackName("testStack");
        AlertManagerPayload.Alert alert = new AlertManagerPayload.Alert();
        alert.setStatus("Firing");
        alert.setAnnotations(new HashMap<String, String>(){{
            put("message","This is an alert");
        }});
        alert.setLabels(new HashMap<String, String>(){{
            put("severity","warning");
            put("pod","test-pod");
            put("type","application");
        }});
        AlertNotification alertNotification = new AlertNotification(cluster, alert);
        String notificationMessage = alertNotification.getNotificationMessage();
        assert notificationMessage.contains("test-pod");
        assert notificationMessage.contains("This is an alert");
        System.out.println(notificationMessage);
    }

}