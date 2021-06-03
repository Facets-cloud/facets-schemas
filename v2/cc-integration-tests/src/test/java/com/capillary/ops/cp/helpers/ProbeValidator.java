package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.bo.StackProbe;
import io.fabric8.kubernetes.api.model.Probe;
import org.junit.Assert;
import org.junit.Assume;
import org.springframework.stereotype.Component;

@Component
public class ProbeValidator {
    public void validate(Probe k8sProbe, StackProbe stackProbe) {
        Assume.assumeTrue("None of the probe types {TCP, HTTP, EXEC } are enabled. Skipping the test case. ",stackProbe.getEnableTCP() || stackProbe.getEnableHTTP() || stackProbe.getEnableEXEC());

        Assert.assertEquals("Failure Threshold is not matching. ", k8sProbe.getFailureThreshold(), stackProbe.getFailureThreshold());
        Assert.assertEquals(" Initial Delay Seconds is not matching. ", k8sProbe.getInitialDelaySeconds(), stackProbe.getInitialDelay());
        Assert.assertEquals("Period Seconds is not matching. ", k8sProbe.getPeriodSeconds(), stackProbe.getPeriod());
        Assert.assertEquals("Success Threshold is not matching. ", k8sProbe.getSuccessThreshold(), stackProbe.getSuccessThreshold());
        Assert.assertEquals("Timeout Seconds is not matching. ", k8sProbe.getTimeoutSeconds(), stackProbe.getTimeout());


        if (stackProbe.getEnableTCP()){
            Assert.assertEquals("TCP Socket Port is not matching. ", k8sProbe.getTcpSocket().getPort().getIntVal(), stackProbe.getPort());
        }
        if (stackProbe.getEnableHTTP()){
            Assert.assertEquals("HttpGetPath is not matching. ", k8sProbe.getHttpGet().getPath(), stackProbe.getHttpEndpoint());
            Assert.assertEquals("HttpGetPort is not matching. ", k8sProbe.getHttpGet().getPort().getIntVal(), stackProbe.getPort());
        }
        if (stackProbe.getEnableEXEC()){
            Assert.assertTrue("Exec commands mentioned in the stack instance are missing", k8sProbe.getExec().getCommand().containsAll(stackProbe.getExecCommands()));
        }
    }

}
