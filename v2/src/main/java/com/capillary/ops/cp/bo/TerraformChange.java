package com.capillary.ops.cp.bo;

public class TerraformChange {

    public static enum TerraformChangeType {
        Destruction,
        Creation,
        Modifications
    }

    private String resourcePath;
    private String resourceKey;
    private TerraformChangeType type;

    public TerraformChange() {
    }

    public TerraformChange(String resourcePath, String resourceKey, TerraformChangeType type) {
        this.resourcePath = resourcePath;
        this.resourceKey = resourceKey;
        this.type = type;
    }

    public TerraformChange(String logMessage) {
        this.resourcePath = resourcePath;
        this.resourceKey = resourceKey;
        this.type = type;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public TerraformChangeType getType() {
        return type;
    }

    public void setType(TerraformChangeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TerraformChange{" +
                "resourcePath='" + resourcePath + '\'' +
                ", resourceKey='" + resourceKey + '\'' +
                ", type=" + type +
                '}';
    }
}
