package com.capillary.ops.cp.service.mocks;

import com.capillary.ops.App;
import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.service.TFBuildService;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.jcabi.aspects.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Loggable
@Profile("dev")
public class MockAwsCodeBuildService implements TFBuildService {

    @Autowired
    private DeploymentLogRepository deploymentLogRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public DeploymentLog deployLatest(AbstractCluster cluster,
                                      DeploymentRequest deploymentRequest,
                                      DeploymentContext deploymentContext)
    {

        DeploymentLog log = new DeploymentLog();
        String codeBuildId = "45kdfslsdlksf" + new Random().nextInt(100) + "iop";
        log.setDeploymentType(DeploymentLog.DeploymentType.CUSTOM);
        log.setCodebuildId(codeBuildId);
        log.setClusterId(cluster.getId());
        log.setDescription(deploymentRequest.getTag());
        log.setReleaseType(deploymentRequest.getReleaseType());
        log.setCreatedOn(new Date());
        log.setStackVersion("mock_secondary_source_version");
        log.setTfVersion("mock_tf_version");
        log.setDeploymentContextVersion("mock_deployment_context_version");
        log.setStatus(StatusType.SUCCEEDED);
        log.setTriggeredBy(deploymentRequest.getTriggeredBy());
        log.setOverrideBuildSteps(deploymentRequest.getOverrideBuildSteps());

        String buildSpec = getBuildSpec(deploymentRequest);
        System.out.println(buildSpec);

        return deploymentLogRepository.save(log);
    }

    @Override
    public DeploymentLog loadDeploymentStatus(DeploymentLog deploymentLog, boolean loadBuildDetails) {

        if(loadBuildDetails){
            Artifact a = new Artifact("mock_artifact_application_name", "mock_artifact_uri", deploymentLog.getCodebuildId(), "mock_artifact_build_description", BuildStrategy.QA, deploymentLog.getReleaseType(), "mock_artifactory");

            List<TerraformChange> tc = getTerraformChanges(deploymentLog.getCodebuildId());
            deploymentLog.setChangesApplied(tc);
            List<AppDeployment> appDeployments = tc.stream()
                    .map(x -> new AppDeployment(x.getResourceKey(), a))
                    .distinct()
                    .collect(Collectors.toList());
            deploymentLog.setAppDeployments(appDeployments);
            deploymentLogRepository.save(deploymentLog);
        }

        return deploymentLog;
    }

    @Override
    public List<TerraformChange> getTerraformChanges(String codeBuildId) {
        return Stream.of(new TerraformChange("mock_resource_path",
                "mock_resource_key",
                TerraformChange.TerraformChangeType.Modifications)).collect(Collectors.toList());
    }

    private String getBuildSpec(DeploymentRequest deploymentRequest) {
        try {
            String buildSpecYaml =
                    CharStreams.toString(
                            new InputStreamReader(
                                    App.class.getClassLoader().getResourceAsStream("cc/cc-buildspec.yaml"),
                                    Charsets.UTF_8));
            Map<String, Object> buildSpec = new Yaml().load(buildSpecYaml);
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true);
            yamlMapper.configure(YAMLGenerator.Feature.SPLIT_LINES, false);
            if(deploymentRequest.getOverrideBuildSteps() == null || deploymentRequest.getOverrideBuildSteps().isEmpty()) {
                if(deploymentRequest.getPreBuildSteps() != null && !deploymentRequest.getPreBuildSteps().isEmpty()){
                    ((List<String>)(((Map<String, Object>) ((Map<String, Object>) buildSpec.get("phases")).get("pre_build"))).get("commands"))
                            .addAll(deploymentRequest.getPreBuildSteps());
                    return yamlMapper.writeValueAsString(buildSpec);
                }
                return buildSpecYaml;
            }
            (((Map<String, Object>) ((Map<String, Object>) buildSpec.get("phases")).get("build")))
                    .put("commands", deploymentRequest.getOverrideBuildSteps());
            return yamlMapper.writeValueAsString(buildSpec);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
