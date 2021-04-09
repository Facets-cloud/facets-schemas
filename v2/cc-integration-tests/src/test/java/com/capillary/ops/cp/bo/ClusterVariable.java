package com.capillary.ops.cp.bo;

public class ClusterVariable {

    private Boolean secret;

    private String value;

    private Boolean exclude_from_app_env;

    public Boolean getSecret() {
        return secret;
    }

    public void setSecret(Boolean secret) {
        this.secret = secret;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getExclude_from_app_env() {
        return exclude_from_app_env;
    }

    public void setExclude_from_app_env(Boolean exclude_from_app_env) {
        this.exclude_from_app_env = exclude_from_app_env;
    }
}
