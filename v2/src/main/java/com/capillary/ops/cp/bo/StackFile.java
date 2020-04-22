package com.capillary.ops.cp.bo;

import java.util.HashMap;
import java.util.Map;

public class StackFile {

    private Map<String, String> stackVariables = new HashMap<>();

    private Map<String, VariableDetails> clusterVariablesMeta = new HashMap<>();

    public Map<String, String> getStackVariables() {
        return stackVariables;
    }

    public void setStackVariables(Map<String, String> stackVariables) {
        this.stackVariables = stackVariables;
    }

    public Map<String, VariableDetails> getClusterVariablesMeta() {
        return clusterVariablesMeta;
    }

    public void setClusterVariablesMeta(Map<String, VariableDetails> clusterVariablesMeta) {
        this.clusterVariablesMeta = clusterVariablesMeta;
    }

    public static class VariableDetails {

        private boolean secret = false;
        private String value = null;

        public VariableDetails() {

        }

        public VariableDetails(boolean secret, String value) {
            this.secret = secret;
            this.value = value;
        }

        public boolean isRequired() {
            return value == null;
        }

        public boolean isSecret() {
            return secret;
        }

        public void setSecret(boolean secret) {
            this.secret = secret;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
}
