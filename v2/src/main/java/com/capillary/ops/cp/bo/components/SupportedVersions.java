package com.capillary.ops.cp.bo.components;

import javax.validation.constraints.NotNull;
import java.util.Set;

public class SupportedVersions {
    public SupportedVersions(@NotNull ComponentType componentType, @NotNull Set<String> supportedVersions) {
        this.componentType = componentType;
        this.supportedVersions = supportedVersions;
    }

    @NotNull
    private ComponentType componentType;

    @NotNull
    private Set<String> supportedVersions;

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Set<String> getSupportedVersions() {
        return supportedVersions;
    }

    public void setSupportedVersions(Set<String> supportedVersions) {
        this.supportedVersions = supportedVersions;
    }
}
