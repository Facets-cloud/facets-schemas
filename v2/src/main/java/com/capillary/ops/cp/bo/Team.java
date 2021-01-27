package com.capillary.ops.cp.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
public class Team {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    Set<TeamResource> resources;

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

    public Set<TeamResource> getResources() {
        return resources;
    }

    public void setResources(Set<TeamResource> resources) {
        this.resources = resources;
    }
}
