package com.capillary.ops.deployer.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document
public class ApplicationFamilyMetadata {
    @Id
    private String id;

    @Indexed(unique = true)
    private ApplicationFamily applicationFamily;

    private Set<String> stacks = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApplicationFamily getApplicationFamily() {
        return applicationFamily;
    }

    public void setApplicationFamily(ApplicationFamily applicationFamily) {
        this.applicationFamily = applicationFamily;
    }

    public Set<String> getStacks() {
        return stacks;
    }

    public void setStacks(Set<String> stacks) {
        this.stacks = stacks;
    }
}
