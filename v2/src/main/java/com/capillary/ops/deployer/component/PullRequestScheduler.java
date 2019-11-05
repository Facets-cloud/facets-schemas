package com.capillary.ops.deployer.component;

import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PullRequest;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.repository.PullRequestRepository;
import com.capillary.ops.deployer.service.VcsServiceSelector;
import com.capillary.ops.deployer.service.interfaces.ICodeBuildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Calendar;
import java.util.List;

@Profile("!dev")
@Component
public class PullRequestScheduler {

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private ICodeBuildService codeBuildService;

    @Autowired
    private BuildRepository buildRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private VcsServiceSelector vcsServiceSelector;

    private static final Logger logger = LoggerFactory.getLogger(PullRequestScheduler.class);

    private static final String BASE_REPORTS_URL = "https://%s/api/%s/applications/%s/tests/%s";

    private static final String DEFAULT_HOST = "deployer.capillary.in";

    @Scheduled(fixedRate = 300 * 1000)
    public void pollInProgressPullRequests() {
        logger.info("checking for pending pull requests for the last 2 days");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        List<PullRequest> openPullRequests = pullRequestRepository.findAllByBuildStatusAndUpdatedAtAfter(
                StatusType.IN_PROGRESS, calendar.getTime());
        processInProgressPullRequest(openPullRequests);
    }

    private String getTestOutputLink(String host, Application application, Build build) {
        if (host == null) {
            host = DEFAULT_HOST;
        }

        String buildRef = build.getCodeBuildId().split(":")[1];
        return String.format(BASE_REPORTS_URL, host, application.getApplicationFamily().name(), application.getName(), buildRef);
    }

    private void processInProgressPullRequest(List<PullRequest> openPullRequests) {
        openPullRequests.parallelStream().forEach(x -> {
            Build build = buildRepository.findById(x.getBuildId()).get();
            Application application = applicationRepository.findById(build.getApplicationId()).get();
            software.amazon.awssdk.services.codebuild.model.Build codeBuild = codeBuildService.getBuild(
                    application, build.getCodeBuildId());
            StatusType statusType = codeBuild.buildStatus();
            if (!statusType.equals(StatusType.IN_PROGRESS)) {
                logger.info("processing pending pull request: {}", x);
                processBuildStatusChanged(application, x, build, codeBuild, statusType);
            }
        });
    }

    private void processBuildStatusChanged(Application application, PullRequest pullRequest, Build build,
                                           software.amazon.awssdk.services.codebuild.model.Build codeBuild,
                                           StatusType statusType) {
        String comment = "";
        String codeBuildId = build.getCodeBuildId();
        switch (statusType) {
            case FAULT:
                logger.error("error happened for application: {}, while building in codebuild: {}",
                        application.getName(), codeBuildId);
                comment = "Error happened while building, please contact admin";
                break;
            case FAILED:
                logger.error("build failed for application: {}, pull request: {}", application.getName(),
                        pullRequest.getNumber());
                comment = "Build failed. Refer to the following build id in deployer: " + build.getId();
                break;
            case TIMED_OUT:
                logger.error("build timed out while building in codebuild: {}", codeBuildId);
                comment = "Build timed out, please reopen the Pull Request";
                break;
            case STOPPED:
                logger.error("build stopped manually for codebuild: {}", codeBuildId);
                comment = "Build stopped manually, please reopen Pull Request to build again";
                break;
            case SUCCEEDED:
                logger.info("build successful for pull request: {}", pullRequest.getNumber());
                comment = "Build Successful";
                break;
            default:
                logger.error("unkown build status");
        }

        if (!StringUtils.isEmpty(codeBuild.artifacts().md5sum())) {
            comment = comment + ". Test Report Output: " + getTestOutputLink(pullRequest.getHost(), application, build);
        }

        vcsServiceSelector.selectVcsService(application.getVcsProvider()).commentOnPullRequest(pullRequest, comment);
        pullRequest.setBuildStatus(statusType);
        pullRequestRepository.save(pullRequest);
    }
}
