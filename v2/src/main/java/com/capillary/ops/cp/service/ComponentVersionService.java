package com.capillary.ops.cp.service;

import com.capillary.ops.App;
import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AzureCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.components.ComponentType;
import com.capillary.ops.cp.bo.requests.AzureClusterRequest;
import com.capillary.ops.cp.bo.requests.ClusterRequest;
import com.capillary.ops.cp.bo.requests.ClusterTaskRequest;
import com.capillary.ops.cp.exceptions.UnsupportedComponentVersionException;
import com.capillary.ops.deployer.exceptions.NotFoundException;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class ComponentVersionService {

    @Autowired
    private MetaService metaService;

    @Autowired
    private StackService stackService;

    @Autowired
    private ClusterTaskService clusterTaskService;

    public void syncComponentsVersion(ClusterRequest request, AbstractCluster existingCluster) {
        Map<ComponentType, String> componentVersions = getClusterComponentVersions(
                request.getStackName(), request);
        for (ComponentType componentType: componentVersions.keySet()) {
            String clusterComponentVersion = existingCluster.getComponentVersions().get(ComponentType.KUBERNETES);
            String newComponentVersion = componentVersions.get(ComponentType.KUBERNETES);
            if (componentType.equals(ComponentType.KUBERNETES) &&
                    !clusterComponentVersion.equals(newComponentVersion)) {
                syncKubernetesComponent(existingCluster, componentVersions);
            }
        }
        existingCluster.setComponentVersions(request.getComponentVersions());
    }

    private void syncKubernetesComponent(AbstractCluster cluster, Map<ComponentType, String> componentVersions)
            throws RuntimeException {
        ClusterTaskRequest clusterTaskRequest = new ClusterTaskRequest();
        clusterTaskRequest.setStackName(cluster.getStackName());
        clusterTaskRequest.setClusterId(cluster.getId());

        String k8sVersion = componentVersions.get(ComponentType.KUBERNETES);
        String upgradeSteps;
        try {
            upgradeSteps = CharStreams.toString(
                    new InputStreamReader(
                            App.class.getClassLoader().getResourceAsStream(String.format("componentUpgrade/k8s/AWS/%s/steps", k8sVersion)),
                            Charsets.UTF_8));
        } catch (IOException e) {
            throw new NotFoundException(String.format("upgrade steps for k8s version: %s not found", k8sVersion));
        }

        List<String> clusterTask = Arrays.asList(upgradeSteps.split("\n"));

        if (!clusterTask.isEmpty()) {
            clusterTaskRequest.setTasks(clusterTask);
            clusterTaskService.createClusterTasks(clusterTaskRequest);
        }
    }

    public Map<ComponentType, String> getClusterComponentVersions(String stackId, ClusterRequest request) {
        Optional<Stack> stackOptional = stackService.getStackById(stackId);
        if (!stackOptional.isPresent()) {
            throw new NotFoundException("no stack found with id: " + stackId);
        }
        Stack stack = stackOptional.get();
        Map<ComponentType, String> componentVersions = request.getComponentVersions();
        Map<ComponentType, String> calculatedVersions = new HashMap<>();
        Map<ComponentType, Set<String>> supportedVersions = metaService.getSupportedVersionsMap();
        String k8sComponentVersion = getK8sComponentVersion(stack, componentVersions, supportedVersions);
        calculatedVersions.put(ComponentType.KUBERNETES, k8sComponentVersion);
        return calculatedVersions;
    }

    private String getK8sComponentVersion(Stack stack, Map<ComponentType, String> componentVersions,
                                          Map<ComponentType, Set<String>> supportedVersions) {
        String stackK8sVersion = stack.getComponentVersions().get(ComponentType.KUBERNETES);
        String k8sVersion = componentVersions.getOrDefault(ComponentType.KUBERNETES, stackK8sVersion);
        Set<String> supportedK8sVersions = supportedVersions.get(ComponentType.KUBERNETES);
        if (!supportedK8sVersions.contains(k8sVersion)) {
            String message = k8sVersion + " is not in the list of supported versions " + supportedK8sVersions;
            throw new UnsupportedComponentVersionException(message);
        }

        if (Float.parseFloat(k8sVersion) > Float.parseFloat(stackK8sVersion)) {
            String message = String.format("cluster k8s version: %s cannot be greater than stack k8s version: %s",
                    k8sVersion, stackK8sVersion);
            throw new UnsupportedComponentVersionException(message);
        }

        return k8sVersion;
    }
}
