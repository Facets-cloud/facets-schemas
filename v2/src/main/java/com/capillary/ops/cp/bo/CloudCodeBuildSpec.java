package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.CloudCodeBuildSpecPhase;
import com.capillary.ops.cp.bo.requests.CloudCodeBuildSpecRequest;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CloudCodeBuildSpec {
    public CloudCodeBuildSpec() {}

    public CloudCodeBuildSpec(String clusterId, Map<String, CloudCodeBuildSpecPhase> buildSpecPhases) {
        this.setVersion("0.2");
        this.setProjectName("capillary-cloud-tf-apply");
        this.setClusterId(clusterId);
        this.setArtifactFiles(null);
        this.setBuildSpecPhases(buildSpecPhases);
    }

    public CloudCodeBuildSpec(CloudCodeBuildSpecRequest request) {
        Map<String, CloudCodeBuildSpecPhase> buildSpecPhases = new HashMap<>();
        buildSpecPhases.put("build", request.getBuildPhase());

        this.setVersion("0.2");
        this.setProjectName("capillary-cloud-tf-apply");
        this.setClusterId(request.getClusterId());
        this.setArtifactFiles(null);
        this.setBuildSpecPhases(buildSpecPhases);
    }

    public CloudCodeBuildSpec(String projectName, String clusterId, String version, Map<String, CloudCodeBuildSpecPhase> buildSpecPhases, List<String> artifactFiles) {
        this.projectName = projectName;
        this.clusterId = clusterId;
        this.version = version;
        this.buildSpecPhases = buildSpecPhases;
        this.artifactFiles = artifactFiles;
    }

    @Id
    private String id;

    private String projectName = "capillary-cloud-tf-apply";

    private String clusterId;

    private String version = "0.2";

    private Map<String, CloudCodeBuildSpecPhase> buildSpecPhases;

    private List<String> artifactFiles;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, CloudCodeBuildSpecPhase> getBuildSpecPhases() {
        return buildSpecPhases;
    }

    public void setBuildSpecPhases(Map<String, CloudCodeBuildSpecPhase> buildSpecPhases) {
        this.buildSpecPhases = buildSpecPhases;
    }

    public List<String> getArtifactFiles() {
        return artifactFiles;
    }

    public void setArtifactFiles(List<String> artifactFiles) {
        this.artifactFiles = artifactFiles;
    }
}
