package com.capillary.ops.cp.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonPropertyOrder({"enableLivenessTCP", "enableLivenessEXEC", "enableLivenessHTTP"})
public class StackProbe {

    @JsonAlias({"enableLivenessTCP", "enableReadinessTCP"})
    private Boolean enableTCP = false;

    @JsonAlias({"enableLivenessEXEC", "enableReadinessEXEC"})
    private Boolean enableEXEC = false;

    @JsonAlias({"enableLivenessHTTP", "enableReadinessHTTP"})
    private Boolean enableHTTP = false;

    @JsonAlias({"livenessFailureThreshold", "readinessFailureThreshold"})
    private Integer failureThreshold;

    @JsonAlias({"livenessInitialDelay", "readinessInitialDelay"})
    @NotNull
    private Integer initialDelay;

    @JsonAlias({"livenessPeriod", "readinessPeriod"})
    @NotNull
    private Integer period;

    @JsonAlias({"livenessSuccessThreshold", "readinessSuccessThreshold"})
    private Integer successThreshold;

    @JsonAlias({"livenessHTTPEndpoint", "readinessHTTPEndpoint"})
    private String httpEndpoint;

    @JsonAlias({"livenessPort", "readinessPort"})
    private Integer port;

    @JsonAlias({"livenessTimeout", "readinessTimeout"})
    private Integer timeout;

    @JsonAlias({"livenessCommand", "readinessCommand"})
    private List<String> execCommands;

    public Boolean getEnableTCP() {
        return enableTCP;
    }

    public void setEnableTCP(Boolean enableTCP) {
        this.enableTCP = enableTCP;
    }

    public Boolean getEnableEXEC() {
        return enableEXEC;
    }

    public void setEnableEXEC(Boolean enableEXEC) {
        this.enableEXEC = enableEXEC;
    }

    public Boolean getEnableHTTP() {
        return enableHTTP;
    }

    public void setEnableHTTP(Boolean enableHTTP) {
        this.enableHTTP = enableHTTP;
    }

    public Integer getFailureThreshold() {
        return failureThreshold;
    }

    public void setFailureThreshold(Integer failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    public Integer getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(Integer initialDelay) {
        this.initialDelay = initialDelay;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getSuccessThreshold() {
        return successThreshold;
    }

    public void setSuccessThreshold(Integer successThreshold) {
        this.successThreshold = successThreshold;
    }

    public String getHttpEndpoint() {
        return httpEndpoint;
    }

    public void setHttpEndpoint(String httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public List<String> getExecCommands() {
        return execCommands;
    }

    public void setExecCommands(List<String> execCommands) {
        this.execCommands = execCommands;
    }

    @Override
    public String toString() {
        return "StackProbe{" +
                "enableTCP=" + enableTCP +
                ", enableEXEC=" + enableEXEC +
                ", enableHTTP=" + enableHTTP +
                ", failureThreshold=" + failureThreshold +
                ", initialDelay=" + initialDelay +
                ", period=" + period +
                ", successThreshold=" + successThreshold +
                ", httpEndpoint='" + httpEndpoint + '\'' +
                ", port=" + port +
                ", timeout=" + timeout +
                ", execCommands=" + execCommands +
                '}';
    }
}
