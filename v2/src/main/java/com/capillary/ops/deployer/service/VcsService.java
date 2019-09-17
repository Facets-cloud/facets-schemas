package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.bo.Application;

import java.io.IOException;
import java.util.List;

public interface VcsService {
    public List<String> getBranches(String owner, String repository) throws IOException;

    public List<String> getTags(String owner, String repository) throws IOException;

    public void createPullRequestWebhook(Application application, String owner, String repository) throws IOException;
}
