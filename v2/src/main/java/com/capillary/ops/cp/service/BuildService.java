package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PromotionIntent;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.List;
import java.util.Optional;

@Service
@Loggable
public class BuildService {

    @Autowired
    private BuildRepository ccBuildRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationFacade applicationFacade;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Given an application Id get its Hotfix Build
     *
     * @param applicationId
     * @return
     */
    public Optional<Build> getHotFixBuild(String applicationId) {
        Optional<Build> rBuild = ccBuildRepository
            .findFirstByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(applicationId,
                PromotionIntent.RELEASE);

        Optional<Build> hfBuild = ccBuildRepository
            .findFirstByApplicationIdAndPromotedIsTrueAndPromotionIntentOrderByTimestampDesc(applicationId,
                PromotionIntent.HOTFIX);

        if (hfBuild.isPresent()) {
            if (!rBuild.isPresent()) {
                return hfBuild;
            } else if (rBuild.get().getTimestamp() < hfBuild.get().getTimestamp()) {
                return hfBuild;
            }
        }
        return Optional.empty();
    }

    /**
     * Given an application Id  get its Release Build
     *
     * @param applicationId
     * @return
     */
    public Optional<Build> getReleaseBuild(String applicationId) {
        return ccBuildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(applicationId);
    }

    /**
     * Given an application Id  get its Latest Staging Build
     *
     * @param applicationId
     * @return
     */
    public Optional<Build> getStagingBuild(String applicationId) {
        return ccBuildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(applicationId);
    }

    /**
     * Given an application Id  get its Latest QA Build
     *
     * @param applicationId
     * @return
     */
    public Optional<Build> getQABuild(String applicationId) {
        List<Build> builds = ccBuildRepository
            .findFirst20ByApplicationIdAndPromotableIsFalseAndPromotedIsFalseAndTestBuildIsFalseOrderByTimestampDesc(
                applicationId);
        if (builds.isEmpty()) {
            return Optional.empty();
        }
        for (Build build : builds) {
            Optional<Build> buildDetails = fetchValidBuildWithImage(applicationId, build);
            if (buildDetails.isPresent()) {
                return buildDetails;
            }
        }
        return Optional.empty();
    }

    private Optional<Build> fetchValidBuildWithImage(String applicationId, Build build) {
        logger.info("Resolved Build for ApplicationId: {} : {}", applicationId, build);
        Optional<Application> application = applicationRepository.findById(applicationId);
        if (application.isPresent()) {
            Build buildDetails = applicationFacade.getBuildDetails(application.get(), build, true);
            if (buildDetails.getStatus() == StatusType.SUCCEEDED && buildDetails.getImage() != null && !buildDetails
                .getImage().isEmpty()) {
                return Optional.of(buildDetails);
            } else if (buildDetails.getStatus() == StatusType.SUCCEEDED && buildDetails.getArtifactUrl() != null
                    && !buildDetails.getArtifactUrl().isEmpty() &&
                    application.get().getApplicationType().equals(Application.ApplicationType.SERVERLESS)) {
                return Optional.of(buildDetails);
            }
        }
        return Optional.empty();
    }

    /**
     * Given an application Id  get its Latest Prod Build
     *
     * @param applicationId
     * @param releaseType
     * @return
     */
    public Optional<Build> getProductionBuild(String applicationId, ReleaseType releaseType) {
        Optional<Build> build = Optional.empty();
        switch (releaseType) {
            case HOTFIX:
                build = this.getHotFixBuild(applicationId);
                if (build.isPresent()) {
                    break;
                }
            case RELEASE:
                build = this.getReleaseBuild(applicationId);
                break;
        }
        return build;
    }

}
