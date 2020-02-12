package com.capillary.ops.cp.bo;

import org.springframework.data.annotation.Id;

public class Stack {

    @Id
    private String name;

    private StackStatus status;

    private String vcsUrl;

    private VCS vcs;

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

    public VCS getVcs() {
        return vcs;
    }

    public void setVcs(VCS vcs) {
        this.vcs = vcs;
    }

    public String getVcsUrl() {
        return vcsUrl;
    }

    public void setVcsUrl(String vcsUrl) {
        this.vcsUrl = vcsUrl;
    }
}
