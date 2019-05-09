package com.capillary.ops.deployer.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@CompoundIndexes({
        @CompoundIndex(unique = true, def = "{'secretName':1, 'environmentName':1, 'applicationFamily':1, 'applicationId':1}", name = "unique_credentials")
})
@Document
public class ApplicationSecret {

    public ApplicationSecret() {}

    public ApplicationSecret(String environmentName, ApplicationFamily applicationFamily, String applicationId, String secretName, String description, SecretStatus secretStatus) {
        this.environmentName = environmentName;
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.secretName = secretName;
        this.secretStatus = secretStatus;
        this.description = description;
    }

    public ApplicationSecret(String environmentName, ApplicationFamily applicationFamily, String applicationId, String secretName, String secretValue, String description, SecretStatus secretStatus) {
        this.environmentName = environmentName;
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.secretName = secretName;
        this.secretValue = secretValue;
        this.secretStatus = secretStatus;
        this.description = description;
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
    private String environmentName;

    @JsonIgnore
    private ApplicationFamily applicationFamily;

    @JsonIgnore
    private String applicationId;

    @JsonView(UserView.SecretName.class)
    private String secretName;

    @Transient
    private String secretValue = "";

    private String description = "";

    @JsonView(UserView.SecretName.class)
    private SecretStatus secretStatus = SecretStatus.UNFULFILLED;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationSecret)) return false;
        ApplicationSecret that = (ApplicationSecret) o;
        return getEnvironmentName().equals(that.getEnvironmentName()) &&
                getApplicationFamily() == that.getApplicationFamily() &&
                getApplicationId().equals(that.getApplicationId()) &&
                getSecretName().equals(that.getSecretName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEnvironmentName(), getApplicationFamily(), getApplicationId(), getSecretName());
    }
}
