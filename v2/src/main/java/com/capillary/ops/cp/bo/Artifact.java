package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.ReleaseType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@CompoundIndex(def = "{'applicationName':1, 'artifactory': 1, 'releaseStream':1, 'releaseType':1}", name = "per_app_one_build")
public class Artifact {

    public Artifact() {
    }

    public Artifact(String applicationName, String artifactUri, String buildId, String buildDescription,
                    BuildStrategy releaseStream, ReleaseType releaseType, String artifactory) {
        this.applicationName = applicationName;
        this.artifactUri = artifactUri;
        this.releaseStream = releaseStream;
        this.releaseType = releaseType;
        this.artifactory = artifactory;
        this.buildId = buildId;
        this.buildDescription = buildDescription;
    }

    @Id
    private String id;
    private String buildId;
    private String applicationName;
    private String artifactUri;
    private BuildStrategy releaseStream;
    private ReleaseType releaseType;
    private String artifactory;
    private String buildDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getArtifactUri() {
        return artifactUri;
    }

    public void setArtifactUri(String artifactUri) {
        this.artifactUri = artifactUri;
    }

    public BuildStrategy getReleaseStream() {
        return releaseStream;
    }

    public void setReleaseStream(BuildStrategy releaseStream) {
        this.releaseStream = releaseStream;
    }

    public ReleaseType getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }

    public String getArtifactory() {
        return artifactory;
    }

    public void setArtifactory(String artifactory) {
        this.artifactory = artifactory;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getBuildDescription() {
        return buildDescription;
    }

    public void setBuildDescription(String buildDescription) {
        this.buildDescription = buildDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return artifactUri.equals(artifact.artifactUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifactUri);
    }
}
