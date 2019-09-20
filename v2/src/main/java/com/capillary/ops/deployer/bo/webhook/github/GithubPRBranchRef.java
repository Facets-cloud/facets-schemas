package com.capillary.ops.deployer.bo.webhook.github;

public class GithubPRBranchRef {

    private String sha;
    private String ref;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
