package com.capillary.ops.cp.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document
@CompoundIndex(def = "{'stackName':1, 'resourceType': 1}", name = "per_stack_one_resourceType", unique = true)
public class AutoCompleteObject {

    public AutoCompleteObject(String stackName, String resourceType) {
        this.stackName = stackName;
        this.resourceType = resourceType;
    }

    @Id
    private String id;
    private String stackName;
    private String resourceType;
    private Set<String> resourceNames = new HashSet<>();

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Set<String> getResourceNames() {
        return resourceNames;
    }

    public void setResourceNames(Set<String> resourceNames) {
        this.resourceNames = resourceNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
