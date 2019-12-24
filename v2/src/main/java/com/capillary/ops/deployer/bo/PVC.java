package com.capillary.ops.deployer.bo;

import java.util.Map;

public class PVC {

    public enum AccessMode {
        ReadWriteOnce,
        ReadOnlyMany,
        ReadWriteMany
    }

    public PVC(){

    }

    public PVC(String name, AccessMode accessMode, Integer storageSize, String volumeDirectory, String mountPath) {
        this.name = name;
        this.accessMode = accessMode;
        this.storageSize = storageSize;
        this.volumeDirectory = volumeDirectory;
        this.mountPath = mountPath;
    }

    private String name;
    private AccessMode accessMode = AccessMode.ReadWriteOnce;
    private Integer storageSize = 1;
    private String volumeDirectory;
    private String mountPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public Integer getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Integer storageSize) {
        this.storageSize = storageSize;
    }

    public String getVolumeDirectory() {
        return volumeDirectory;
    }

    public void setVolumeDirectory(String volumeDirectory) {
        this.volumeDirectory = volumeDirectory;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }
}
