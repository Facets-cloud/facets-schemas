package com.capillary.ops.deployer.service;

import com.capillary.ops.cp.bo.Artifact;
import com.capillary.ops.cp.bo.BuildStrategy;
import com.capillary.ops.cp.bo.requests.ReleaseType;
import com.capillary.ops.cp.facade.ArtifactFacade;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ECRRegistry;
import com.capillary.ops.deployer.bo.PromotionIntent;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.repository.RegistryRepository;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.codebuild.model.Build;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Service
@Loggable
public class CCArtifactCallbackService {

    @Autowired
    private ArtifactFacade artifactFacade;

    @Autowired
    private RegistryRepository registryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CCArtifactCallbackService.class);

    @Value("${deployer.china.url}")
    private String deployerChinaUrl;

    @Value("${deployer.authtoken}")
    private String authToken;

    public void registerArtifact(Build codeBuildServiceBuild, com.capillary.ops.deployer.bo.Build build, Application application){
        if(StatusType.SUCCEEDED.equals(codeBuildServiceBuild.buildStatus()) &&
                ! build.isTestBuild() &&
                !StringUtils.isEmpty(build.getImage())) {
            artifactFacade.registerArtifact(new Artifact(build.getApplicationId(), build.getImage(), build.getId(),
                    build.getDescription(),
                    build.isPromotable() ? BuildStrategy.STAGING : BuildStrategy.QA,
                    build.getPromotionIntent().equals(PromotionIntent.HOTFIX) ?
                            ReleaseType.HOTFIX : ReleaseType.RELEASE,
                    "deployer"));

            if (build.isPromotable()){
                List<Registry> registries = new ArrayList<>();
                registryRepository.findAllById(application.getTagBuildRepositoryIds()).forEach(registries::add);
                List<String> additionalImageIds = new ArrayList<>();
                registries.forEach(registry -> {
                    ECRRegistry ecrRegistry = (ECRRegistry) registry;
                    if (ecrRegistry.getAwsRegion().equals("cn-north-1")){
                        String image = registry.getUri() + "/" + application.getApplicationFamily().name().toLowerCase() + "/" + application.getName() + ":" + codeBuildServiceBuild.resolvedSourceVersion().substring(0, 7) + "-" + codeBuildServiceBuild.buildNumber();
                        additionalImageIds.add(image);
                    }
                });
                build.setAdditionalRepositoryImages(additionalImageIds);
            }
        }
    }

    public void registerProdArtifact(com.capillary.ops.deployer.bo.Build existingBuild, String buildId){
        artifactFacade.registerArtifact(new Artifact(existingBuild.getApplicationId(), existingBuild.getImage(), buildId,
                existingBuild.getDescription(),
                BuildStrategy.PROD,
                ReleaseType.RELEASE,
                "deployer"));

        // if there are additional different ecr registry repository images associated with the current build
        // we create the artifactory of them in those registry account's cc mongo.
        if (existingBuild.getAdditionalRepositoryImages() != null && !existingBuild.getAdditionalRepositoryImages().isEmpty()){
            existingBuild.getAdditionalRepositoryImages().forEach(image -> {
                logger.info("additional images found in the build {}. registering the artifact", image);
                Artifact artifact = new Artifact(existingBuild.getApplicationId(), image, buildId, existingBuild.getDescription(), BuildStrategy.PROD, ReleaseType.RELEASE, "deployer");
                registerArtifactCCCall(artifact);
            });
        }

        if (existingBuild.getPromotionIntent().equals(PromotionIntent.HOTFIX)) {
            artifactFacade.registerArtifact(new Artifact(existingBuild.getApplicationId(), existingBuild.getImage(), buildId,
                    existingBuild.getDescription(),
                    BuildStrategy.PROD,
                    ReleaseType.HOTFIX,
                    "deployer"));

            // the above release logic is applicable even for hotfix builds.
            if (existingBuild.getAdditionalRepositoryImages() != null && !existingBuild.getAdditionalRepositoryImages().isEmpty()){
                existingBuild.getAdditionalRepositoryImages().forEach(image -> {
                    logger.info("additional images found in the build {}. registering the artifact", image);
                    Artifact artifact = new Artifact(existingBuild.getApplicationId(), existingBuild.getImage(), buildId, existingBuild.getDescription(), BuildStrategy.PROD, ReleaseType.HOTFIX, "deployer");
                    registerArtifactCCCall(artifact);
                });
            }
        }
    }

    private void registerArtifactCCCall(Artifact artifact){
        String url = deployerChinaUrl + "/cc/v1/artifacts/register";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-DEPLOYER-INTERNAL-AUTH-TOKEN", authToken);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Artifact> httpEntity = new HttpEntity<>(artifact, httpHeaders);
        restTemplate.postForObject(url, httpEntity, Object.class);
        logger.info("api call made to china to register artifact");
    }
}
