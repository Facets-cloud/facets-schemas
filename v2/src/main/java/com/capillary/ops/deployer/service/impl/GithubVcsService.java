package com.capillary.ops.deployer.service.impl;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PullRequest;
import com.capillary.ops.deployer.bo.webhook.github.SupportedActions;
import com.capillary.ops.deployer.component.DeployerHttpClient;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.repository.PullRequestRepository;
import com.capillary.ops.deployer.service.VcsService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryHook;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GithubVcsService implements VcsService {

    @Autowired
    private DeployerHttpClient httpClient;

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private BuildRepository buildRepository;

    private static final Logger logger = LoggerFactory.getLogger(GithubVcsService.class);

    private static final String PR_WEBHOOK_URL = "https://deployer.capillary.in/api/%s/applications/%s/webhooks/pr/github";

    @Override
    public String getName() {
        return "GITHUB";
    }

    @Override
    public List<String> getBranches(String owner, String repository) throws IOException {
        String username = System.getenv("GITHUB_USERNAME");
        String password = System.getenv("GITHUB_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("github credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Github credentials to use this api");
        }

        RepositoryService repositoryService = new RepositoryService();
        repositoryService.getClient().setCredentials(username, password);

        RepositoryId repositoryId = new RepositoryId(owner, repository);
        logger.debug("fetching github branches");
        List<RepositoryBranch> branches = repositoryService.getBranches(repositoryId);
        logger.info("fetched {} branches from github for this application", branches.size());

        return branches.parallelStream().map(x -> x.getName()).collect(Collectors.toList());
    }

    @Override
    public List<String> getTags(String owner, String repository) throws IOException {
        String username = System.getenv("GITHUB_USERNAME");
        String password = System.getenv("GITHUB_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("github credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Github credentials to use this api");
        }

        RepositoryService repositoryService = new RepositoryService();
        repositoryService.getClient().setCredentials(username, password);

        RepositoryId repositoryId = new RepositoryId(owner, repository);
        logger.debug("fetching github tags");
        List<RepositoryTag> tags = repositoryService.getTags(repositoryId);
        logger.info("fetched {} tags from github for this application", tags.size());

        return tags.parallelStream().map(x -> x.getName()).collect(Collectors.toList());
    }

    private boolean pullRequestWebhookExists(RepositoryService repositoryService, RepositoryId repositoryId) throws IOException {
        List<RepositoryHook> existingHooks = repositoryService.getHooks(repositoryId);
        List<RepositoryHook> webhooks = existingHooks.stream()
                .filter(x -> x.getName().equals("web") && x.isActive())
                .collect(Collectors.toList());
        for (RepositoryHook hook: webhooks) {
            List<String> events = hook.getEvents();
            if (events.contains("pull_request") && events.contains("repository")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void createPullRequestWebhook(Application application, String owner, String repository) throws IOException {
        String username = System.getenv("GITHUB_USERNAME");
        String password = System.getenv("GITHUB_PASSWORD");

        RepositoryId repositoryId = new RepositoryId(owner, repository);
        RepositoryService repositoryService = new RepositoryService();
        repositoryService.getClient().setCredentials(username, password);

        if (!pullRequestWebhookExists(repositoryService, repositoryId)) {
            String webhookURL = String.format(PR_WEBHOOK_URL, application.getApplicationFamily(), application.getId());
            RepositoryHook repositoryHook = new RepositoryHook()
                    .setName("web")
                    .setEvents(Lists.newArrayList("pull_request", "repository"))
                    .setActive(true)
                    .setConfig(ImmutableMap.of(
                            "url", webhookURL,
                            "content_type", "json",
                            "insecure_ssl", "0"));
            repositoryService.createHook(repositoryId, repositoryHook);
        }
    }

    @Override
    public void processPullRequest(PullRequest pullRequest, Build build) {
        pullRequest.setBuildId(build.getId());
        pullRequest.setBuildStatus(StatusType.IN_PROGRESS);
        pullRequestRepository.save(pullRequest);
    }

    @Override
    public void commentOnPullRequest(PullRequest pullRequest, String content) {
        String username = System.getenv("GITHUB_USERNAME");
        String password = System.getenv("GITHUB_PASSWORD");

        JSONObject comments = new JSONObject();
        comments.put("body", content);

        try {
            httpClient.makePOSTRequest(pullRequest.getCommentsUrl(), comments.toString(), username, password);
        } catch (IOException e) {
            logger.error("could not connect to github vcs service");
        }
    }

    @Override
    public boolean shouldTriggerBuild(Application application, PullRequest pullRequest) {
        boolean actionSupported = this.getSupportedActions().contains(pullRequest.getAction());
        if (!actionSupported) {
            logger.info("following pull request action is not supported for pr: {}", pullRequest);
            return false;
        }

        List<PullRequest> existingPullRequests = pullRequestRepository.findAllByApplicationIdAndSha(application.getId(), pullRequest.getSha());
        if (!existingPullRequests.isEmpty()) {
            logger.error("pull request with same sha already exists: {}", pullRequest);
            this.commentOnPullRequest(pullRequest, "Pull request with same diff is already present, not triggering build");
            return false;
        }

        return true;
    }

    @Override
    public List<String> getSupportedActions() {
        return Stream.of(SupportedActions.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
