package com.capillary.ops.deployer.bo;

import java.io.Serializable;
import java.util.Date;

public class ApplicationMetrics implements Serializable {

    // generic
    private String applicationId;
    private Date date;

    // deployer
    private Integer buildFailures;
    private Integer unitTestCoverage;
    private Integer unitTests;
    private Integer criticalCodeSmells;

    // automation
    private Integer regressionCoverage;
    private Integer regressionFailures;


    //  new relic
    private Integer alerts;
    private Integer errors;
    private Double responseTime;
    private Integer fatalErrorsFromLogs;
    private Integer outages;
    private Integer issuesReported;

    // meta data
    private String sonarUrl;


    public ApplicationMetrics(String applicationId, Date date) {
        this.applicationId = applicationId;
        this.date = date;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getBuildFailures() {
        return buildFailures;
    }

    public void setBuildFailures(Integer buildFailures) {
        this.buildFailures = buildFailures;
    }

    public Integer getUnitTestCoverage() {
        return unitTestCoverage;
    }

    public void setUnitTestCoverage(Integer unitTestCoverage) {
        this.unitTestCoverage = unitTestCoverage;
    }

    public Integer getUnitTests() {
        return unitTests;
    }

    public void setUnitTests(Integer unitTests) {
        this.unitTests = unitTests;
    }

    public Integer getCriticalCodeSmells() {
        return criticalCodeSmells;
    }

    public void setCriticalCodeSmells(Integer criticalCodeSmells) {
        this.criticalCodeSmells = criticalCodeSmells;
    }

    public Integer getRegressionCoverage() {
        return regressionCoverage;
    }

    public void setRegressionCoverage(Integer regressionCoverage) {
        this.regressionCoverage = regressionCoverage;
    }

    public Integer getRegressionFailures() {
        return regressionFailures;
    }

    public void setRegressionFailures(Integer regressionFailures) {
        this.regressionFailures = regressionFailures;
    }

    public Integer getAlerts() {
        return alerts;
    }

    public void setAlerts(Integer alerts) {
        this.alerts = alerts;
    }

    public Integer getErrors() {
        return errors;
    }

    public void setErrors(Integer errors) {
        this.errors = errors;
    }

    public Double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Double responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getFatalErrorsFromLogs() {
        return fatalErrorsFromLogs;
    }

    public void setFatalErrorsFromLogs(Integer fatalErrorsFromLogs) {
        this.fatalErrorsFromLogs = fatalErrorsFromLogs;
    }

    public Integer getOutages() {
        return outages;
    }

    public void setOutages(Integer outages) {
        this.outages = outages;
    }

    public Integer getIssuesReported() {
        return issuesReported;
    }

    public void setIssuesReported(Integer issuesReported) {
        this.issuesReported = issuesReported;
    }

    public String getSonarUrl() {
        return sonarUrl;
    }

    public void setSonarUrl(String sonarUrl) {
        this.sonarUrl = sonarUrl;
    }

    public ApplicationMetrics() {
    }
}
