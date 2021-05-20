package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.bo.StackIngressRule;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import org.junit.Assert;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IngressValidator {

    public void validate(Ingress k8sIngress, StackIngressRule stackIngress) {

        Assert.assertEquals("Service name is not matching with that of stack definition.", stackIngress.getK8sService(), k8sIngress.getSpec().getRules().get(0).getHttp().getPaths().get(0).getBackend().getServiceName());
        Assert.assertEquals("Ingress class is not matching with that of stack definition.", stackIngress.getIngressClass(), k8sIngress.getMetadata().getAnnotations().get("kubernetes.io/ingress.class"));
        Assert.assertEquals("Proxy Connect Timeout is not matching with that of stack definition.", stackIngress.getTimeout().toString(), k8sIngress.getMetadata().getAnnotations().get("nginx.ingress.kubernetes.io/proxy-connect-timeout"));
        Assert.assertEquals("Proxy Send Timeout is not matching with that of stack definition.", stackIngress.getTimeout().toString(), k8sIngress.getMetadata().getAnnotations().get("nginx.ingress.kubernetes.io/proxy-send-timeout"));
        Assert.assertEquals("Proxy Read Timeout is not matching with that of stack definition.", stackIngress.getTimeout().toString(), k8sIngress.getMetadata().getAnnotations().get("nginx.ingress.kubernetes.io/proxy-read-timeout"));

        if(stackIngress.getBasicAuthEnabled()){
            Assert.assertTrue("Auth Realm not found with basic auth enabled for the stack instance.", Optional.ofNullable(k8sIngress.getMetadata().getAnnotations().get("nginx.ingress.kubernetes.io/auth-realm")).isPresent());
            Assert.assertTrue("Auth Secret not found with basic auth enabled for the stack instance.", Optional.ofNullable(k8sIngress.getMetadata().getAnnotations().get("nginx.ingress.kubernetes.io/auth-secret")).isPresent());
            Assert.assertTrue("Auth Type not found with basic auth enabled for the stack instance.", Optional.ofNullable(k8sIngress.getMetadata().getAnnotations().get("nginx.ingress.kubernetes.io/auth-type")).isPresent());
        }

        Assert.assertEquals("Host is not matching as per the stack instance definition.", stackIngress.getHost(), k8sIngress.getSpec().getRules().get(0).getHost());
        Assert.assertEquals("Path is not matching as per the stack instance definition.", stackIngress.getPath(), k8sIngress.getSpec().getRules().get(0).getHttp().getPaths().get(0).getPath());
        Assert.assertEquals("Target Port is not matching as per the stack instance definition.", stackIngress.getTargetPort(), k8sIngress.getSpec().getRules().get(0).getHttp().getPaths().get(0).getBackend().getServicePort().getIntVal());

    }
}
