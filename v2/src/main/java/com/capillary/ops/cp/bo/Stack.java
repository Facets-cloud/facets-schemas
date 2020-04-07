package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.Map;

public class Stack {

    @Id
    private String name;

    private String vcsUrl;

    private VCS vcs;

    private String relativePath;

    private String user;

    private String appPassword;

    @JsonIgnore
    private Map<String, String> stackVars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public Map<String, String> getStackVars() {
        return stackVars;
    }

    public void setStackVars(Map<String, String> stackVars) {
        this.stackVars = stackVars;
    }
}
