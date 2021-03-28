package com.capillary.ops.cp.bo;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class AlertManagerPayload {

    public static final String NO_NAME = "No Name";
    public static final String NO_TYPE = "No Type";
    public static final String ALERTNAME = "alertname";


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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public HashMap<String, String> getLabels() {
            return labels;
        }

        public void setLabels(HashMap<String, String> labels) {
            this.labels = labels;
        }

        public HashMap<String, String> getAnnotations() {
            return annotations;
        }

        public void setAnnotations(HashMap<String, String> annotations) {
            this.annotations = annotations;
        }

        public String getStartsAt() {
            return startsAt;
        }

        public void setStartsAt(String startsAt) {
            this.startsAt = startsAt;
        }

        public String getEndsAt() {
            return endsAt;
        }

        public void setEndsAt(String endsAt) {
            this.endsAt = endsAt;
        }

        public String getGeneratorURL() {
            return generatorURL;
        }

        public void setGeneratorURL(String generatorURL) {
            this.generatorURL = generatorURL;
        }
    }

    public static class Response {
        private String version;
        private String groupKey;
        private int truncatedAlerts;
        private String status;
        private String receiver;
        private HashMap<String, String> groupLabels;
        private String externalURL;
        private List<Alert> alerts;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getGroupKey() {
            return groupKey;
        }

        public void setGroupKey(String groupKey) {
            this.groupKey = groupKey;
        }

        public int getTruncatedAlerts() {
            return truncatedAlerts;
        }

        public void setTruncatedAlerts(int truncatedAlerts) {
            this.truncatedAlerts = truncatedAlerts;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public HashMap<String, String> getGroupLabels() {
            return groupLabels;
        }

        public void setGroupLabels(HashMap<String, String> groupLabels) {
            this.groupLabels = groupLabels;
        }

        public String getExternalURL() {
            return externalURL;
        }

        public void setExternalURL(String externalURL) {
            this.externalURL = externalURL;
        }

        public List<Alert> getAlerts() {
            return alerts;
        }

        public void setAlerts(List<Alert> alerts) {
            this.alerts = alerts;
        }
    }
}
