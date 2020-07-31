package com.capillary.ops.deployer.bo;

import com.capillary.ops.deployer.bo.webhook.sonar.Condition;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TestBuildDetails {


    @Id
    private String id;

    @NotNull
    private String buildId;

    private String applicationId;
    private String applicationFamily;
    private Long timestamp = new Date().getTime();


    private String prId;
    private String branch;
    private String branchType;

    private String sonarUrl;

    private Status testStatus;

    // Property to value
    private List<Condition> testStatusRules = new ArrayList<>();

    public enum Status {
        PASS, FAIL, TEST_FAILED, INPROGRESS
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public Status getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(Status testStatus) {
        this.testStatus = testStatus;
    }

    public List<Condition> getTestStatusRules() {
        testStatusRules.sort(Comparator.comparing(Condition::getStatus));
        return testStatusRules;
    }

    public void setTestStatusRules(List<Condition> testStatusRules) {
        this.testStatusRules = testStatusRules;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(String applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public String getSonarUrl() {
        return sonarUrl;
    }

    public void setSonarUrl(String sonarUrl) {
        this.sonarUrl = sonarUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }
}
