package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.bo.PromotionIntent;
import com.capillary.ops.deployer.repository.BuildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuildService {

    @Autowired
    private BuildRepository ccBuildRepository;

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
        return ccBuildRepository
            .findFirstByApplicationIdAndPromotableIsFalseAndPromotedIsFalseOrderByTimestampDesc(applicationId);
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
