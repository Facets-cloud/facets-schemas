package com.capillary.ops.cp.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndex(def = "{'teamId':1, 'userName':1}", name = "singleMembership")
public class TeamMembership {
    @Id
    private String id;

    private String teamId;

    private String userName;

    public TeamMembership() {
    }

    public TeamMembership(String teamId, String userName) {
        this.teamId = teamId;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
