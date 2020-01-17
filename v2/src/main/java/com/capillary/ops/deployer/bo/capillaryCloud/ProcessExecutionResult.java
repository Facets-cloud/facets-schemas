package com.capillary.ops.deployer.bo.capillaryCloud;

import org.springframework.data.annotation.Id;

public class ProcessExecutionResult {

    @Id
    private String id;
    private Integer exitValue;
    private String stdOut;
    private String stdErr;


    public ProcessExecutionResult() {
    }

    public ProcessExecutionResult(Integer exitValue, String stdOut, String stdErr) {
        this.exitValue = exitValue;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    public String getStdOut() {
        return stdOut;
    }

    public void setStdOut(String stdOut) {
        this.stdOut = stdOut;
    }

    public String getStdErr() {
        return stdErr;
    }

    public void setStdErr(String stdErr) {
        this.stdErr = stdErr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExitValue(Integer exitValue) {
        this.exitValue = exitValue;
    }

    public Integer getExitValue() {
        return exitValue;
    }
}
