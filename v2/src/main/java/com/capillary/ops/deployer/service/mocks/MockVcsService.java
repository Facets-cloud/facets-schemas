package com.capillary.ops.deployer.service.mocks;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PullRequest;
import com.capillary.ops.deployer.service.VcsService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Profile("dev")
@Service
public class MockVcsService implements VcsService {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getBranches(String owner, String repository) throws IOException {
        return Arrays.asList("branch1", "branch2", "branch3", "branch4", "branch5", "branch3", "branch4", "branch5", "branch3", "branch4", "branch5");
    }

    @Override
    public List<String> getTags(String owner, String repository) throws IOException {
        return Arrays.asList("tag1", "tag2", "tag3");
    }

    @Override
    public String createPullRequestWebhook(Application application, String owner, String repository, String host) throws IOException {
        return null;
    }

    @Override
    public void processPullRequest(PullRequest pullRequest, Build build) {

    }

    @Override
    public void commentOnPullRequest(PullRequest pullRequest, String content) {

    }

    @Override
    public boolean shouldTriggerBuild(Application application, PullRequest pullRequest) {
        return false;
    }

    @Override
    public List<String> getSupportedActions() {
        return null;
    }
}
