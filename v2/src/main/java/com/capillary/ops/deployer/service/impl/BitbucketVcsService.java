package com.capillary.ops.deployer.service.impl;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PullRequest;
import com.capillary.ops.deployer.bo.webhook.bitbucket.SupportedActions;
import com.capillary.ops.deployer.component.DeployerHttpClient;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.PullRequestRepository;
import com.capillary.ops.deployer.service.VcsService;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BitbucketVcsService implements VcsService {

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private DeployerHttpClient httpClient;

    private static final Logger logger = LoggerFactory.getLogger(BitbucketVcsService.class);

    private static final String BASE_URI = "https://api.bitbucket.org/2.0";
    private static final String PR_WEBHOOK_URL = "https://deployer.capillary.in/api/%s/applications/%s/webhooks/pr/bitbucket";


    private List<JSONObject> getPaginatedResponse(String requestUri, String username, String password) throws IOException {


        JSONObject currentPage = httpClient.makeGETRequest(requestUri, username, password);
        List<JSONObject> responseValues = convertJsonArrayToList(currentPage);
        List<JSONObject> jsonValues = new ArrayList<>(responseValues);

        for (int page = 2; page <= currentPage.getInt("pagelen") ; page++) {
            if (responseValues.size() > 0 && currentPage.has("next")) {
                currentPage = httpClient.makeGETRequest(currentPage.getString("next"), username, password);
                jsonValues.addAll(convertJsonArrayToList(currentPage));
            }
        }

        return jsonValues;
    }

    private List<JSONObject> convertJsonArrayToList(JSONObject jsonObject) {
        JSONArray responseValues = jsonObject.getJSONArray("values");
        List<JSONObject> values = Lists.newArrayListWithExpectedSize(responseValues.length());
        for (Object object : responseValues) {
            values.add((JSONObject) object);
        }

        return values;
    }

    @Override
    public String getName() {
        return "BITBUCKET";
    }

    @Override
    public List<String> getBranches(String owner, String repository) throws IOException {
        String username = System.getenv("BITBUCKET_USERNAME");
        String password = System.getenv("BITBUCKET_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("bitbucket credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Bitbucket credentials to use this api");
        }

        String requestUri = new StringJoiner("/")
                .add(BASE_URI)
                .add("repositories")
                .add(owner)
                .add(repository)
                .add("refs")
                .add("branches")
                .toString();

        List<JSONObject> jsonValues = this.getPaginatedResponse(requestUri, username, password);

        return jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());
    }

    @Override
    public List<String> getTags(String owner, String repository) throws IOException {
        String username = System.getenv("BITBUCKET_USERNAME");
        String password = System.getenv("BITBUCKET_PASSWORD");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("bitbucket credentials not found, please fulfill the credentials to use this api");
            throw new NotFoundException("Please fulfill the Bitbucket credentials to use this api");
        }

        String requestUri = new StringJoiner("/")
                .add(BASE_URI)
                .add("repositories")
                .add(owner)
                .add(repository)
                .add("refs")
                .add("tags")
                .toString();

        List<JSONObject> jsonValues = this.getPaginatedResponse(requestUri, username, password);

        return jsonValues.parallelStream().map(x -> x.getString("name")).collect(Collectors.toList());
    }

    private String getPRWebhookBody(Application application) {
        JSONObject webhook = new JSONObject();
        webhook.put("description", "web");
        webhook.put("active", true);
        webhook.put("url", String.format(PR_WEBHOOK_URL, application.getApplicationFamily(), application.getId()));
        webhook.put("events", Lists.newArrayList("pullrequest:created", "pullrequest:updated"));

        return webhook.toString();
    }

    @Override
    public void createPullRequestWebhook(Application application, String owner, String repository) throws IOException {
        String username = System.getenv("BITBUCKET_USERNAME");
        String password = System.getenv("BITBUCKET_PASSWORD");

        String requestUri = new StringJoiner("/")
                .add(BASE_URI)
                .add("repositories")
                .add(owner)
                .add(repository)
                .add("hooks")
                .toString();

        String webhookBody = getPRWebhookBody(application);
        httpClient.makePOSTRequest(requestUri, webhookBody, username, password);
    }

    @Override
    public void processPullRequest(PullRequest pullRequest, Build build) {
        pullRequest.setBuildId(build.getId());
        pullRequest.setApplicationId(build.getApplicationId());
        pullRequest.setBuildStatus(StatusType.IN_PROGRESS);
        pullRequestRepository.save(pullRequest);
    }

    @Override
    public void commentOnPullRequest(PullRequest pullRequest, String content) {
        String username = System.getenv("BITBUCKET_USERNAME");
        String password = System.getenv("BITBUCKET_PASSWORD");

        JSONObject raw = new JSONObject();
        raw.put("raw", content);
        JSONObject comments = new JSONObject();
        comments.put("content", raw);

        try {
            httpClient.makePOSTRequest(pullRequest.getCommentsUrl(), comments.toString(), username, password);
        } catch (IOException e) {
            logger.error("could not connect to bitbucket vcs service");
        }
    }

    @Override
    public boolean shouldTriggerBuild(Application application, PullRequest pullRequest) {
        boolean actionSupported = this.getSupportedActions().contains(pullRequest.getAction());
        if (!actionSupported) {
            logger.info("following pull request action is not supported for pr: {}", pullRequest);
            return false;
        }

        List<PullRequest> existingPullRequests = pullRequestRepository.findAllByApplicationIdAndSha(application.getId(),
                pullRequest.getSha());
        if (!existingPullRequests.isEmpty()) {
            logger.error("pull request with same sha already exists: {}", pullRequest);
            this.commentOnPullRequest(pullRequest, "Pull request with same diff is already present, " +
                    "not triggering build");
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
