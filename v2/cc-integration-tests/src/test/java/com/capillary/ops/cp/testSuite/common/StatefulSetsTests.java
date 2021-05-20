package com.capillary.ops.cp.testSuite.common;

import com.capillary.ops.cp.App;
import com.capillary.ops.cp.bo.InstancePort;
import com.capillary.ops.cp.bo.PVC;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.bo.StackIngressRule;
import com.capillary.ops.cp.bo.StackProbe;
import com.capillary.ops.cp.helpers.CommonUtils;
import com.capillary.ops.cp.helpers.IngressValidator;
import com.capillary.ops.cp.helpers.ProbeValidator;
import com.capillary.ops.cp.helpers.StackTestUtils;
import com.capillary.ops.cp.helpers.k8s.K8sTestUtils;
import com.google.gson.JsonObject;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.extensions.Ingress;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@TestPropertySource(locations = "classpath:test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {App.class})
@SpringBootTest
public class StatefulSetsTests {
    private static final Logger logger = LoggerFactory.getLogger(StatefulSetsTests.class);

    private static Map<String, JsonObject> stackStatefulSetsInstances = new HashMap<>();

    private static Map<String, StatefulSet> k8sStackStatefulSets = new HashMap<>();

    @Autowired
    private StackTestUtils stackTestUtils;

    @Autowired
    private K8sTestUtils k8sTestUtils;

    @Autowired
    private ProbeValidator probeValidator;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private IngressValidator ingressValidator;

    private static final String STATEFUL_SETS = "statefulsets";

    private static final String STATEFUL_SETS_NAMESPACE = "default";

    private static final String cpuUnits = "";

    private static final String memoryUnits = "Gi";

    private void setStatefulSetsProbeDefaults(StackProbe probe) {
        if (probe.getEnableTCP() || probe.getEnableHTTP()) {
            probe.setFailureThreshold(3);
            probe.setSuccessThreshold(1);
            probe.setTimeout(1);
        }
    }

    @Before
    public void populateInstances() {
        if (stackStatefulSetsInstances.size() == 0) {
            stackStatefulSetsInstances = stackTestUtils.sampleInstances(STATEFUL_SETS);
            k8sStackStatefulSets = k8sTestUtils.getK8sStatefulSets(stackStatefulSetsInstances.keySet(), STATEFUL_SETS_NAMESPACE);
        }
    }

    @Test
    public void verifySize() {
        k8sStackStatefulSets.forEach((statefulSet, statefulSetInstance) -> {
            logger.info("Sampled {} from the statefulsets.", statefulSet);
            try{
                Map<String, Quantity> limits = statefulSetInstance.getSpec().getTemplate().getSpec().getContainers().get(0).getResources().getLimits();
                PodSize k8sStatefulSetSize = new PodSize(k8sTestUtils.getK8sCpuSize(limits.get("cpu").getAmount()), k8sTestUtils.getK8sMemorySize(limits.get("memory").getAmount()));
                PodSize instanceSizing = stackTestUtils.getInstanceSizingWithUnits(STATEFUL_SETS, stackStatefulSetsInstances.get(statefulSet), cpuUnits, memoryUnits);

                Assert.assertEquals("Statefulset size should match with the type mentioned in the instance in terms of the sizing defined for the statefulsets instances. ", k8sStatefulSetSize, instanceSizing);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void verifyPorts() {
        k8sStackStatefulSets.forEach((statefulSet, statefulSetInstance) -> {
            logger.info("Sampled {} from the statefulsets.", statefulSet);

            List<ContainerPort> containerPorts = statefulSetInstance.getSpec().getTemplate().getSpec().getContainers().get(0).getPorts();
            List<InstancePort> instancePorts = stackTestUtils.getInstancePorts(stackStatefulSetsInstances.get(statefulSet));
            List<Boolean> booleanList = instancePorts.stream().map(port -> containerPorts.stream().anyMatch(containerPort -> containerPort.getName().equals(port.getName()) && containerPort.getContainerPort().equals(port.getContainerPort()))).collect(Collectors.toList());
            booleanList.forEach(Assert::assertTrue);
        });
    }

    @Test
    public void verifyLiveness() {
        k8sStackStatefulSets.forEach((statefulSet, statefulSetInstance) -> {
            logger.info("Sampled {} from the statefulsets.", statefulSet);

            Probe livenessK8sProbe = statefulSetInstance.getSpec().getTemplate().getSpec().getContainers().get(0).getLivenessProbe();
            Optional<StackProbe> livenessStackProbe = stackTestUtils.getStackProbe(stackStatefulSetsInstances.get(statefulSet), "liveness");
            Assume.assumeTrue("Liveness Probe is Empty. Skipping the test case. ", livenessStackProbe.isPresent());

            setStatefulSetsProbeDefaults(livenessStackProbe.get());

            probeValidator.validate(livenessK8sProbe, livenessStackProbe.get());
        });
    }

    @Test
    public void verifyReadiness() {
        k8sStackStatefulSets.forEach((statefulSet, statefulSetInstance) -> {
            logger.info("Sampled {} from the statefulsets.", statefulSet);

            Probe readinessK8sProbe = statefulSetInstance.getSpec().getTemplate().getSpec().getContainers().get(0).getReadinessProbe();
            Optional<StackProbe> readinessStackProbe = stackTestUtils.getStackProbe(stackStatefulSetsInstances.get(statefulSet), "readiness");
            Assume.assumeTrue("Readiness probe is empty. Skipping the test case. ", readinessStackProbe.isPresent());

            setStatefulSetsProbeDefaults(readinessStackProbe.get());

            probeValidator.validate(readinessK8sProbe, readinessStackProbe.get());

        });
    }

    @Test
    public void verifyIngressRules() {
        String clusterName = commonUtils.getClusterName();
        k8sStackStatefulSets.forEach((statefulSet, statefulSetInstance) -> {
            logger.info("Sampled {} from the statefulsets.", statefulSet);
            try {
                Map<String, StackIngressRule> ingressRules = stackTestUtils.getIngressRules(stackStatefulSetsInstances.get(statefulSet), statefulSet, clusterName);
                Assume.assumeTrue("Couldn't find any ingress rules.", ingressRules.size() > 0);

                ingressRules.forEach((ingressName, stackIngress) -> {
                    try {
                        Ingress k8sIngress = k8sTestUtils.getK8sIngress(ingressName).orElseThrow(() -> new RuntimeException("Couldn't find ingress rule " + ingressName + " in the cluster. "));
                        ingressValidator.validate(k8sIngress, stackIngress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void verifyVolumeClaimTemplates() {
        k8sStackStatefulSets.forEach((statefulSet, statefulSetInstance) -> {
            logger.info("Sampled {} from the statefulsets. ", statefulSet);

            List<PVC> instancePVCs = stackTestUtils.getInstancePVCs(stackStatefulSetsInstances.get(statefulSet));
            List<PersistentVolumeClaim> volumeClaimTemplates = statefulSetInstance.getSpec().getVolumeClaimTemplates();
            List<VolumeMount> volumeMounts = statefulSetInstance.getSpec().getTemplate().getSpec().getContainers().get(0).getVolumeMounts();

            instancePVCs.forEach(pvc -> {
                String pvcName = pvc.getName() + "-vol";
                String pvcStorage = pvc.getStorageSize()+"Gi";
                String pvcMountPath = "/mnt/"+pvc.getMountPath();

                boolean pvcMatch = volumeClaimTemplates.stream().anyMatch(persistentVolumeClaim -> pvcName.equals(persistentVolumeClaim.getMetadata().getName()) && pvc.getAccessMode().equals(persistentVolumeClaim.getSpec().getAccessModes().get(0)) && pvcStorage.equals(persistentVolumeClaim.getSpec().getResources().getRequests().get("storage").getAmount()));
                Assert.assertTrue(String.format("PVC %s is not found in the statefulset %s. ", pvc.getName(), statefulSet), pvcMatch);

                boolean volumeMountMatch = volumeMounts.stream().anyMatch(volumeMount -> volumeMount.getMountPath().equals(pvcMountPath) && volumeMount.getName().equals(pvcName));
                Assert.assertTrue(String.format("Volume mount for PVC %s is not found in the statefulset %s. ", pvc.getName(), statefulSet), volumeMountMatch);
            });
        });
    }

}
