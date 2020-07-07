package com.capillary.ops.deployer.bo.webhook.sonar;


import java.util.List;

public class QualityGate{
    private String status;
    private List<Condition> conditions;
    public QualityGate() {
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
}