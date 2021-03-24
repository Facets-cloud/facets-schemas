package com.capillary.ops.cp.bo;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class AlertManagerPayload {

    public static final String NO_NAME = "No Name";
    public static final String NO_TYPE = "No Type";
    public static final String ALERTNAME = "alertname";


    @Getter
    public static class Alert {
        private String status;
        private HashMap<String, String> labels;
        private HashMap<String, String> annotations;
        private String startsAt;
        private String endsAt;
        private String generatorURL;

        public String getMessage() {
            if (annotations.containsKey("message"))
                return annotations.get("message");
            return "No Message";
        }

        public String getResourceType(){
            if (labels.containsKey("resourceType"))
                return labels.get("resourceType");
            return NO_TYPE;
        }

        public String getResourceName(){
            if (labels.containsKey("resourceName"))
                return labels.get("resourceName");
            return NO_NAME;
        }

        public String getSeverity(){
            if (labels.containsKey("severity"))
                return labels.get("severity");
            return "No Severity";
        }
    }

    @Getter
    public static class Response {
        private String version;
        private String groupKey;
        private int truncatedAlerts;
        private String status;
        private String receiver;
        private HashMap<String, String> groupLabels;
        private String externalURL;
        private List<Alert> alerts;
    }
}
