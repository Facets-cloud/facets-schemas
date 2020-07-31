package com.capillary.ops.deployer.bo.webhook.sonar;

public class Branch {

    private String name;
    private String type;
    private String url;

    public Branch(){

    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public String getUrl(){
        return url;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setUrl(String url){
        this.url = url;
    }


}
