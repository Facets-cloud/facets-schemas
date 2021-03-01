package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
public abstract class Artifactory {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotNull
    @Indexed(unique = true, name = "artifactory_name_idx")
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Indexed(unique = true)
    private String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
