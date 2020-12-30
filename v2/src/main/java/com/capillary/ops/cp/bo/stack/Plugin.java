package com.capillary.ops.cp.bo.stack;

public class Plugin {
    private String name;
    private Boolean disabled = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
