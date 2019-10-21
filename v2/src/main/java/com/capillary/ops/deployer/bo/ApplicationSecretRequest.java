package com.capillary.ops.deployer.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@CompoundIndexes({
        @CompoundIndex(unique = true, def = "{'secretName':1, 'applicationFamily':1, 'applicationId':1}", name = "unique_credentials")
})
@Document
public class ApplicationSecretRequest {

    public enum SecretType {
        ENVIRONMENT,
        FILE
    }

    public ApplicationSecretRequest() {}

    public ApplicationSecretRequest(ApplicationFamily applicationFamily, String applicationId, String secretName, String description) {
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.secretName = secretName;
        this.description = description;
    }

    public ApplicationSecretRequest(ApplicationFamily applicationFamily, String applicationId, String secretName, String description, SecretType secretType, String mountPath) {
        this.applicationFamily = applicationFamily;
        this.applicationId = applicationId;
        this.secretName = secretName;
        this.description = description;
        this.secretType = secretType;
    }

    @Id
    @JsonIgnore
    private String id;

    @JsonIgnore
    private ApplicationFamily applicationFamily;

    @JsonIgnore
    private String applicationId;

    @JsonView(UserView.SecretName.class)
    private String secretName;

    private String description = "";

    private SecretType secretType = SecretType.ENVIRONMENT;

    // private String mountPath;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public SecretType getSecretType() {
        return secretType;
    }

    public void setSecretType(SecretType secretType) {
        this.secretType = secretType;
    }

//    public String getMountPath() {
//        return mountPath;
//    }
//
//    public void setMountPath(String mountPath) {
//        this.mountPath = mountPath;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationSecretRequest request = (ApplicationSecretRequest) o;
        return applicationFamily == request.applicationFamily &&
                applicationId.equals(request.applicationId) &&
                secretName.equals(request.secretName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationFamily, applicationId, secretName);
    }
}
