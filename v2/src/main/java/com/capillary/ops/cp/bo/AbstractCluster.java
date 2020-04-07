package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.Cloud;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Cluster agnostic definition of a cluster
 */
public abstract class AbstractCluster {

    @Id
    private String id;

    private String name;

    private Cloud cloud;

    private String tz;

    private String stackName;

    private Map<String, String> commonEnvironmentVariables = new HashMap<>();

    public AbstractCluster(String name, Cloud cloud) {
        this.name = name;
        this.cloud = cloud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cloud getCloud() {
        return cloud;
    }

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
    }

    public Map<String, String> getCommonEnvironmentVariables() {
        return commonEnvironmentVariables.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void addCommonEnvironmentVariables(Map<String, String> commonEnvironmentVariables) {
        this.commonEnvironmentVariables.putAll(commonEnvironmentVariables);
    }

    public String getTz() {
        return tz;
    }

    public void setTz(TimeZone tz) {
        this.tz = tz.getDisplayName();
    }
}
