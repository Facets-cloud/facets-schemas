package com.capillary.ops.cp.facade;

import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.service.BuildService;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.capillary.ops.deployer.bo.Build;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.capillary.ops.deployer.repository.ApplicationRepository;
import com.capillary.ops.deployer.service.facade.ApplicationFacade;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Loggable
public class BuildFacade {

    @Autowired
    BuildService buildService;

    @Autowired
    private ArtifactFacade artifactFacade;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Given an Application Id get a Build Image
     *
     * @param applicationId Application Id
     * @param strategy      Strategy to choose build
     * @param releaseType
     * @return Image name
     */
    public Build getImageFromDeployer(String applicationId, BuildStrategy strategy, ReleaseType releaseType) {
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
            Build result = build.get();
            artifactFacade.registerArtifact(new Artifact(applicationId, result.getImage(), result.getId(),
                    result.getDescription(), strategy, releaseType, "deployer"));
            return result;
        }
        throw new NotFoundException("No Build Found");
    }
}
