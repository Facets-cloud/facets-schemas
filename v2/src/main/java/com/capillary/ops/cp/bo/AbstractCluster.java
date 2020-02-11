package com.capillary.ops.cp.bo;

import com.capillary.ops.cp.bo.requests.Cloud;
import org.springframework.data.annotation.Id;

/**
 * Cluster agnostic definition of a cluster
 */
public abstract class AbstractCluster {

    @Id
    private String id;

    private String name;

    private Cloud cloud;

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
}
