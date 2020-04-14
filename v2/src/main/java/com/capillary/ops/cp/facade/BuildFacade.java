package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.service.BuildService;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.repository.BuildRepository;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component

public class BuildFacade {

    @Autowired
    BuildService buildService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationFacade applicationFacade;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Given an Application Id get a Build Image
     *
     * @param applicationId Application Id
     * @param strategy      Strategy to choose build
     * @param releaseType
     * @return Image name
     */
    public String getImageFromDeployer(String applicationId, BuildStrategy strategy, ReleaseType releaseType) {
        Optional<Build> build = Optional.empty();
        switch (strategy) {

            case QA:
                build = buildService.getQABuild(applicationId);
                break;
            case STAGING:
                build = buildService.getStagingBuild(applicationId);
                break;
            case PROD:
                build = buildService.getProductionBuild(applicationId, releaseType);
                break;
        }
        if (build.isPresent()) {
            logger.info("Resolved Build for ApplicationId: {} : {}", applicationId, build.get());
            ApplicationFamily family = ApplicationFamily.CRM;
            Application application = applicationRepository.findOneByApplicationFamilyAndId(family,
                applicationId).get();
            Build buildDetails = applicationFacade.getBuildDetails(application, build.get(), true);
            return buildDetails.getImage();
        }
        throw new NotFoundException("No Build Found");
    }
}
