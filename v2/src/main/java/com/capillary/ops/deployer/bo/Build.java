package com.capillary.ops.deployer.bo;

import com.capillary.ops.deployer.bo.webhook.sonar.Condition;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

public class Build implements Serializable {

    @Id
    private String id;
    @Indexed(name = "cb_id_index", unique = true)
    private String codeBuildId;
    @NotNull
    private String applicationId;
    private ApplicationFamily applicationFamily;
    @NotNull
    private String tag;
    private StatusType status;
    private Map<String, String> environmentVariables;
    private Long timestamp = new Date().getTime();
    private String image;
    private String triggeredBy;
    private String description;
    private boolean promoted = false;
    private boolean unpromoted = true;
    private PromotionIntent promotionIntent = PromotionIntent.NA;
    private boolean promotable = false;
    private String artifactUrl;
    private boolean testBuild = false;

    private static final long serialVersionUID = 3595193021269728012L;

    public Build() {
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeBuildId() {
        return codeBuildId;
    }

    public void setCodeBuildId(String codeBuildId) {
        this.codeBuildId = codeBuildId;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public boolean isUnpromoted() {
        return unpromoted;
    }

    public void setUnpromoted(boolean unpromoted) {
        this.unpromoted = unpromoted;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public boolean isTestBuild() {
        return testBuild;
    }

    public void setTestBuild(boolean testBuild) {
        this.testBuild = testBuild;
    }

    @Override
    public String toString() {
        return "Build{" +
                "id='" + id + '\'' +
                ", codeBuildId='" + codeBuildId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationFamily=" + applicationFamily +
                ", tag='" + tag + '\'' +
                ", status=" + status +
                ", environmentVariables=" + environmentVariables +
                ", timestamp=" + timestamp +
                ", image='" + image + '\'' +
                ", triggeredBy='" + triggeredBy + '\'' +
                ", description='" + description + '\'' +
                ", promoted=" + promoted +
                ", promotionIntent=" + promotionIntent +
                ", promotable=" + promotable +
                ", testBuild=" + testBuild +
                ", artifactUrl='" + artifactUrl + '\'' +
                '}';
    }

    public boolean isPromotable() {
        return promotable;
    }

    public void setPromotable(boolean promotable) {
        this.promotable = promotable;
    }

    public PromotionIntent getPromotionIntent() {
        return promotionIntent;
    }

    public void setPromotionIntent(PromotionIntent promotionIntent) {
        this.promotionIntent = promotionIntent;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }
}
