package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;

public class K8sCredentials {

    @Id
    private String id;
    @JsonIgnore
    @Indexed(name = "cluster_id_index", unique = true)
    private String clusterId;
    @NotNull
    private String kubernetesApiEndpoint;
    @NotNull
    private String kubernetesToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getKubernetesApiEndpoint() {
        return kubernetesApiEndpoint;
    }

    public void setKubernetesApiEndpoint(String kubernetesApiEndpoint) {
        this.kubernetesApiEndpoint = kubernetesApiEndpoint;
    }

    public String getKubernetesToken() {
        return kubernetesToken;
    }

    public void setKubernetesToken(String kubernetesToken) {
        this.kubernetesToken = kubernetesToken;
    }
}
