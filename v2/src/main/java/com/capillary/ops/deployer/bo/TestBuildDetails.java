package com.capillary.ops.deployer.bo;

import com.capillary.ops.deployer.bo.webhook.sonar.Condition;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    private String destBranch;

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

    public String getDestBranch() {
        return destBranch;
    }

    public void setDestBranch(String destBranch) {
        this.destBranch = destBranch;
    }

    public Status getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(Status testStatus) {
        this.testStatus = testStatus;
    }

    public List<Condition> getTestStatusRules() {
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
}
