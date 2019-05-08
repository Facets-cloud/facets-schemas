package com.capillary.ops.deployer.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

public class ApplicationSecret {

    public ApplicationSecret() {}

    public ApplicationSecret(ApplicationFamily applicationFamily, String applicationId, String secretName, SecretStatus secretStatus) {
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.secretName = secretName;
        this.secretStatus = secretStatus;
    }

    public ApplicationSecret(ApplicationFamily applicationFamily, String applicationId, String secretName, String secretValue, SecretStatus secretStatus) {
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.secretName = secretName;
        this.secretValue = secretValue;
        this.secretStatus = secretStatus;
    }

    public enum SecretStatus {
        FULFILLED,
        UNFULFILLED,
        PENDING
    }

    @Id
    @JsonIgnore
    private String id;

    @JsonIgnore
    private ApplicationFamily applicationFamily;

    @JsonIgnore
    private String applicationId;

    @JsonView(UserView.SecretName.class)
    @Indexed(unique = true)
    private String secretName;

    @Transient
    private String secretValue = "";

    @JsonView(UserView.SecretName.class)
    private SecretStatus secretStatus = SecretStatus.UNFULFILLED;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }

    public String getSecretValue() {
        return secretValue;
    }

    public void setSecretValue(String secretValue) {
        this.secretValue = secretValue;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public SecretStatus getSecretStatus() {
        return secretStatus;
    }

    public void setSecretStatus(SecretStatus secretStatus) {
        this.secretStatus = secretStatus;
    }
}
