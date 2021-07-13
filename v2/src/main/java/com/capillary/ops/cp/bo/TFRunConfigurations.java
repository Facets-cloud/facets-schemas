package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Document
public class TFRunConfigurations {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @Indexed(unique = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String clusterId;

    private Map<String, String> additionalEnvVars = new HashMap<>();

    private String branchOverride;

    public String getId() {
        return id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public Map<String, String> getAdditionalEnvVars() {
        return additionalEnvVars;
    }

    public void setAdditionalEnvVars(Map<String, String> additionalEnvVars) {
        this.additionalEnvVars = additionalEnvVars;
    }

    public String getBranchOverride() {
        return branchOverride;
    }

    public void setBranchOverride(String branchOverride) {
        this.branchOverride = branchOverride;
    }
}
