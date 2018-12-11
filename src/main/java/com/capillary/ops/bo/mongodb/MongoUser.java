package com.capillary.ops.bo.mongodb;

import com.capillary.ops.bo.Environments;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;

public class MongoUser {

    @Id
    @JsonIgnore
    private String id;

    private String dbName;

    private String userName;

    private String password;

    private List<String> userRoles;

    private String appName;

    private Environments environment;

    public String getDbName() {
        return dbName;
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppName() {
        return appName;
    }

    public Environments getEnvironment() {
        return environment;
    }

    @Override
    public String toString() {
        return "MongoUser{" +
                "dbName='" + dbName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userRoles=" + userRoles +
                ", appName='" + appName + '\'' +
                ", environment=" + environment +
                '}';
    }
}
