package com.capillary.ops.deployer.bo.webhook.github;

public class GithubPRReview {
    private Long id;
    private GithubUser user;
    private String state;

    public String getUserName(){
        return this.user.getLogin();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GithubUser getUser() {
        return user;
    }

    public void setUser(GithubUser user) {
        this.user = user;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(state);
    }
}
