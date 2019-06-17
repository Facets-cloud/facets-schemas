package com.capillary.ops.deployer.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@CompoundIndexes({
        @CompoundIndex(name = "unique_applicationFamilyAndName", unique = true,
                def = "{'environmentMetaData.name':1, 'environmentMetaData.applicationFamily':1}")
})
@Document
public class Environment {
    @Id
    private String id;
    private EnvironmentMetaData environmentMetaData;
    private EnvironmentConfiguration environmentConfiguration;

    public EnvironmentMetaData getEnvironmentMetaData() {
        return environmentMetaData;
    }

    public void setEnvironmentMetaData(EnvironmentMetaData environmentMetaData) {
        this.environmentMetaData = environmentMetaData;
    }

    public EnvironmentConfiguration getEnvironmentConfiguration() {
        return environmentConfiguration;
    }

    public void setEnvironmentConfiguration(EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
