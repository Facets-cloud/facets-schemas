package com.capillary.ops.deployer.bo.actions;

public class ActionExecution {

    private ApplicationAction action;
    private TriggerStatus triggerStatus;
    private Long triggerTime;
    private Exception triggerException;

    public ActionExecution(ApplicationAction action, TriggerStatus triggerStatus, Long triggerTime) {
        this.action = action;
        this.triggerStatus = triggerStatus;
        this.triggerTime = triggerTime;
    }

    public ActionExecution(ApplicationAction action, TriggerStatus triggerStatus, Long triggerTime, Exception triggerException) {
        this(action, triggerStatus, triggerTime);
        this.triggerException = triggerException;
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

    public Exception getTriggerException() {
        return triggerException;
    }

    public void setTriggerException(Exception triggerException) {
        this.triggerException = triggerException;
    }

    @Override
    public String toString() {
        return "ActionExecution{" +
                "action=" + action +
                ", triggerStatus=" + triggerStatus +
                ", triggerTime=" + triggerTime +
                ", triggerException=" + triggerException +
                '}';
    }
}
