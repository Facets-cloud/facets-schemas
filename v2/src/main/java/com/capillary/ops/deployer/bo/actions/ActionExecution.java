package com.capillary.ops.deployer.bo.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

public class ActionExecution {

    @Id
    private String id;
    private String applicationId;
    private ApplicationAction action;
    private TriggerStatus triggerStatus;
    private Long triggerTime;
    private String output;
    private String triggerException;

    public ActionExecution() {
    }

    public ActionExecution(String applicationId, ApplicationAction action, TriggerStatus triggerStatus, Long triggerTime) {
        this.applicationId = applicationId;
        this.action = action;
        this.triggerStatus = triggerStatus;
        this.triggerTime = triggerTime;
    }

    public ActionExecution(ApplicationAction action, TriggerStatus triggerStatus, Long triggerTime) {
        this(null, action, triggerStatus, triggerTime);
    }

    public ActionExecution(String applicationId, ApplicationAction action, TriggerStatus triggerStatus,
                           Long triggerTime, String triggerException) {
        this(applicationId, action, triggerStatus, triggerTime);
        this.triggerException = triggerException;
    }

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

    public ApplicationAction getAction() {
        return action;
    }

    public void setAction(ApplicationAction action) {
        this.action = action;
    }

    public TriggerStatus getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(TriggerStatus triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    public Long getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Long triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getTriggerException() {
        return triggerException;
    }

    public void setTriggerException(String triggerException) {
        this.triggerException = triggerException;
    }

    @Override
    public String toString() {
        return "ActionExecution{" +
                "applicationId='" + applicationId + '\'' +
                ", action=" + action +
                ", triggerStatus=" + triggerStatus +
                ", triggerTime=" + triggerTime +
                ", output='" + output + '\'' +
                ", triggerException=" + triggerException +
                '}';
    }
}
