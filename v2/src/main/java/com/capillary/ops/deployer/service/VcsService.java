package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PullRequest;

import java.io.IOException;
import java.util.List;

public interface VcsService {
    public String getName();

    public List<String> getBranches(String owner, String repository) throws IOException;

    public List<String> getTags(String owner, String repository) throws IOException;

    public String createPullRequestWebhook(Application application, String owner, String repository, String host) throws IOException;

    public void processPullRequest(PullRequest pullRequest, Build build);

    public void commentOnPullRequest(PullRequest pullRequest, String content);

    public boolean shouldTriggerBuild(Application application, PullRequest pullRequest);

    public List<String> getSupportedActions();

    public String getDefaultBranchName(String owner, String repository) throws IOException;

    void approvePullRequest(PullRequest pullRequest) throws IOException;

    void rejectPullRequest(PullRequest pullRequest) throws IOException;
}
