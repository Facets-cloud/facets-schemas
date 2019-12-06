package com.capillary.ops.deployer.bo.actions;

import com.capillary.ops.deployer.bo.BuildType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.Map;

public class ApplicationAction {
    @Id
    private String id;

    private String name;

    private String applicationId;

    private ActionType actionType;

    private String path;

    private String arguments = "";

    private String argumentsRegex = "";

    private BuildType buildType;

    private CreationStatus creationStatus;

    private Map<String, Object> metadata;

    public ApplicationAction() {
    }

    public ApplicationAction(String id, String name, String applicationId, ActionType actionType, String path,
                             String arguments, String argumentsRegex, BuildType buildType, CreationStatus creationStatus, Map<String, Object> metadata) {
        this.id = id;
        this.name = name;
        this.applicationId = applicationId;
        this.actionType = actionType;
        this.path = path;
        this.arguments = arguments;
        this.argumentsRegex = argumentsRegex;
        this.buildType = buildType;
        this.creationStatus = creationStatus;
        this.metadata = metadata;
    }

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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getArgumentsRegex() {
        return argumentsRegex;
    }

    public void setArgumentsRegex(String argumentsRegex) {
        this.argumentsRegex = argumentsRegex;
    }

    public BuildType getBuildType() {
        return buildType;
    }

    public void setBuildType(BuildType buildType) {
        this.buildType = buildType;
    }

    public CreationStatus getCreationStatus() {
        return creationStatus;
    }

    @JsonIgnore
    public void setCreationStatus(CreationStatus creationStatus) {
        this.creationStatus = creationStatus;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ApplicationAction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", actionType=" + actionType +
                ", path='" + path + '\'' +
                ", arguments='" + arguments + '\'' +
                ", buildType=" + buildType +
                ", creationStatus=" + creationStatus +
                ", metadata=" + metadata +
                '}';
    }
}
