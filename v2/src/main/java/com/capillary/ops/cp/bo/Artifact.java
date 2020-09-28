package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.ReleaseType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndex(def = "{'applicationName':1, 'artifactory': 1, 'releaseStream':1, 'releaseType':1}", name = "per_app_one_build")
public class Artifact {

    public Artifact() {
    }

    public Artifact(String applicationName, String artifactUri,
                    BuildStrategy releaseStream, ReleaseType releaseType, String artifactory) {
        this.applicationName = applicationName;
        this.artifactUri = artifactUri;
        this.releaseStream = releaseStream;
        this.releaseType = releaseType;
        this.artifactory = artifactory;
    }

    @Id
    private String id;
    private String applicationName;
    private String artifactUri;
    private BuildStrategy releaseStream;
    private ReleaseType releaseType;
    private String artifactory;

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
}
