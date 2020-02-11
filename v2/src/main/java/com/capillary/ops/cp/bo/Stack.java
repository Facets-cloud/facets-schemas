package com.capillary.ops.cp.bo;

import org.springframework.data.annotation.Id;

public class Stack {

    @Id
    private String name;

    private StackStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StackStatus getStatus() {
        return status;
    }

    public void setStatus(StackStatus status) {
        this.status = status;
    }
}
