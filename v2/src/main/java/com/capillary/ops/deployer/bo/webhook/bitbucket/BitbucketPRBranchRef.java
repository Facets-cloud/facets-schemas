package com.capillary.ops.deployer.bo.webhook.bitbucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BitbucketPRBranchRef {

    private String sha;

    private String branchName;

    @JsonProperty(value = "commit")
    private void unpackCommitFromSource(Map<String, Object> commit) {
        sha = (String) commit.get("hash");
    }

    @JsonProperty(value = "branch")
    private void unpackBranchFromSource(Map<String, String> branch) {
        branchName = branch.get("name");
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
