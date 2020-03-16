package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.BuildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BuildFacade {

    @Autowired
    private BuildRepository ccBuildRepository;

    /**
     * Given an Application Id get a Build Image
     *
     * @param applicationId Application Id
     * @param strategy      Strategy to choose build
     * @return Image name
     */
    public String getImageFromDeployer(String applicationId, BuildStrategy strategy) {
        Optional<Build> build = Optional.empty();
        switch (strategy) {

            case QA:
                build =
                    ccBuildRepository.findFirstByApplicationIdAndPromotableIsFalseOrderByTimestampDesc(applicationId);
                break;
            case STAGING:
                build =
                    ccBuildRepository.findFirstByApplicationIdAndPromotableIsTrueOrderByTimestampDesc(applicationId);
                break;
            case PROD:
                build = ccBuildRepository.findFirstByApplicationIdAndPromotedIsTrueOrderByTimestampDesc(applicationId);
                break;
        }
        if (build.isPresent()) {
            return build.get().getImage();
        }
        throw new NotFoundException("No Build Found");
    }
}
