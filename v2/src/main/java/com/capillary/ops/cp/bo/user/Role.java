package com.capillary.ops.cp.bo.user;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {
    ADMIN("CC-ADMIN", "Gives Admin Access to all functionality", true),
    VIEWER("VIEWER", "View only access", true),
    GUEST("CC-GUEST", "Guest User role which is created on login", true),
    CLUSTER_ADMIN("CLUSTER_ADMIN", "A user with cluster management privileges", true),
    K8S_READER("K8S_READER", "Read Credentials for Kubernetes", false),
    K8S_DEBUGGER("K8S_DEBUGGER", "Debugging credentials for Kubernetes", false);

    private final String id;
    private final String description;
    private final boolean isBaseRole;

    Role(String id, String description, boolean b) {
        this.id = id;
        this.description = description;
        this.isBaseRole = b;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return this.name();
    }

    public boolean isBaseRole() {
        return isBaseRole;
    }
}
