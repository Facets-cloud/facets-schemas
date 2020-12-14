package com.capillary.ops.cp.service.mocks;

import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.requests.DeploymentRequest;
import com.capillary.ops.cp.repository.DeploymentLogRepository;
import com.capillary.ops.cp.service.TFBuildService;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.codebuild.model.StatusType;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Loggable
@Profile("dev")
public class MockAwsCodeBuildService implements TFBuildService {

    @Autowired
    private DeploymentLogRepository deploymentLogRepository;

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
}
