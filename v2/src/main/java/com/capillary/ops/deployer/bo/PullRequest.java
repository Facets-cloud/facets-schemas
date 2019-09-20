package com.capillary.ops.deployer.bo;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@CompoundIndexes({
        @CompoundIndex(unique = true, def = "{'applicationId':1, 'sha':1}", name = "unique_sha"),
        @CompoundIndex(def = "{'updatedAt':1, 'buildStatus':1}", name = "pending_pull_requests")
})
@Document
public class PullRequest {

    @Id
    private String id;

    private String applicationId;

    private String action;

    private int number;

    private String url;

    private String state;

    private String sourceBranch;

    private String destinationBranch;

    @Indexed
    private Date updatedAt;

    private String sha;

    private String commentsUrl;

    @Indexed
    private String buildId;

    @Indexed
    private StatusType buildStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public String getDestinationBranch() {
        return destinationBranch;
    }

    public void setDestinationBranch(String destinationBranch) {
        this.destinationBranch = destinationBranch;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public StatusType getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(StatusType buildStatus) {
        this.buildStatus = buildStatus;
    }

    public static PullRequest fromJSON(JSONObject hook) throws ParseException {
        JSONObject pr = hook.getJSONObject("pul_request");
        PullRequest webhook = new PullRequest();
        webhook.setNumber(pr.getInt("number"));
        webhook.setCommentsUrl(pr.getString("comments_url"));
        webhook.setSha(pr.getJSONObject("head").getString("sha"));
        webhook.setUrl(pr.getString("url"));
        webhook.setState(pr.getString("state"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = simpleDateFormat.parse(pr.getString("updated_at"));
        webhook.setUpdatedAt(date);

        return webhook;
    }

    @Override
    public String toString() {
        return "PullRequest{" +
                "id='" + id + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", action='" + action + '\'' +
                ", number=" + number +
                ", url='" + url + '\'' +
                ", state='" + state + '\'' +
                ", sourceBranch='" + sourceBranch + '\'' +
                ", destinationBranch='" + destinationBranch + '\'' +
                ", updatedAt=" + updatedAt +
                ", sha='" + sha + '\'' +
                ", commentsUrl='" + commentsUrl + '\'' +
                ", buildId='" + buildId + '\'' +
                ", buildStatus=" + buildStatus +
                '}';
    }
}
