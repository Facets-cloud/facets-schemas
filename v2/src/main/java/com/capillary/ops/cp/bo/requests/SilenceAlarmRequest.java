package com.capillary.ops.cp.bo.requests;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.HashMap;

public class SilenceAlarmRequest {

    @NotNull
    private HashMap<String,String> labels;

    @NotNull
    private Integer snoozeInHours;

    @NotNull
    private String comment;

    public HashMap<String, String> getLabels() {
        return labels;
    }

    public void setLabels(HashMap<String, String> labels) {
        this.labels = labels;
    }

    public Integer getSnoozeInHours() {
        return snoozeInHours;
    }

    public void setSnoozeInHours(Integer snoozeInHours) {
        this.snoozeInHours = snoozeInHours;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
