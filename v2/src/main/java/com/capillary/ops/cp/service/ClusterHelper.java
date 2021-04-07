package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.*;
import com.capillary.ops.cp.bo.Stack;
import com.google.common.collect.Maps;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Loggable
public class ClusterHelper {

    public static final String TOOLS_NAME_IN_RESOURCES = "Basic Authentication Password";
    public static final String TOOLS_PASS_KEY_IN_RESOURCES = "tools";
    public static final String TOOLS_USER = "toolsuser";
    @Autowired
    private ClusterResourceRefreshService clusterResourceRefreshService;

    /**
     * Get all common variables from stack and cluster definitions
     *
     * @param clusterObj
     * @param stackObj
     * @return
     */
    public Map<String, String> getCommonVariables(AbstractCluster clusterObj, Stack stackObj) {
        Map<String, String> stackVars = stackObj.getStackVars();
        stackVars.put("CLUSTER", clusterObj.getName());
        stackVars.put("TZ", clusterObj.getTz());
        if (clusterObj instanceof AwsCluster) {
            stackVars.put("AWS_REGION", ((AwsCluster) clusterObj).getAwsRegion());
        }
        Map<String, String> userVars = clusterObj.getUserInputVars();
        Predicate<Map.Entry<String, StackFile.VariableDetails>> isNotSecret =
                e -> !e.getValue().isSecret();
        Map<String, String> requestBasedVars = transformVars(userVars, stackObj.getClusterVariablesMeta(), isNotSecret);
        stackVars.putAll(requestBasedVars);
        return stackVars;
    }

    /**
     * Get all secrets from stack and cluster definitions
     *
     * @param clusterObj
     * @param stackObj
     * @return
     */
    public Map<String, String> getSecrets(AbstractCluster clusterObj, Stack stackObj) {
        Map<String, String> clusterVars = clusterObj.getUserInputVars();
        Predicate<Map.Entry<String, StackFile.VariableDetails>> isCommonEnv =
                e -> e.getValue().isSecret();
        Map<String, String> secrets = transformVars(clusterVars, stackObj.getClusterVariablesMeta(), isCommonEnv);
        return secrets;
    }

    /**
     * Validate and get all ClusterVars
     *
     * @param clusterVars
     * @param stackObj
     */
    public Map<String, String> validateClusterVars(Map<String, String> clusterVars, Stack stackObj) {
        Map<String, StackFile.VariableDetails> stackClusterVariables = stackObj.getClusterVariablesMeta();

        List<String> requiredKeys =
                stackClusterVariables.entrySet().stream().filter(e -> e.getValue().isRequired())
                        .map(Map.Entry::getKey).collect(Collectors.toList());

        List<String> allValidKeys =
                new ArrayList<>(stackClusterVariables.keySet());

        if (!clusterVars.keySet().containsAll(requiredKeys)) {
            throw new IllegalArgumentException("Not all required keys are specified, Required keys: " + requiredKeys);
        }

        if (!allValidKeys.containsAll(clusterVars.keySet())) {
            throw new IllegalArgumentException("Unrecognised key specified, Recognized keys: " + allValidKeys);
        }

        return clusterVars;
    }

    private Map<String, String> transformVars(Map<String, String> clusterVars,
                                              Map<String, StackFile.VariableDetails> stackDefinition,
                                              Predicate<Map.Entry<String, StackFile.VariableDetails>> predicate) {
        Map<String, StackFile.VariableDetails> filteredVars = stackDefinition.entrySet().stream().filter(predicate).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        HashMap<String, String> clusterVarValues = Maps.newHashMapWithExpectedSize(filteredVars.size());
        filteredVars.forEach((k, v) -> {
            String clusterVal = clusterVars.get(k);
            String value = clusterVal == null ? v.getValue() : clusterVal;
            clusterVarValues.put(k, value);
        });

        return clusterVarValues;
    }

    public String getToolsURL(AbstractCluster cluster) {
        return cluster.getName() + ".cctools.capillarytech.com";
    }

    public String getToolsPws(AbstractCluster cluster) {
        List<ResourceDetails> clusterResourceDetails;
        String pwd = "";
        try {
            clusterResourceDetails = clusterResourceRefreshService.isSaveClusterResourceDetailsDone(cluster.getId());
            Optional<ResourceDetails> resourceDetails = clusterResourceDetails.stream().filter((x) -> x.getName().equals(TOOLS_NAME_IN_RESOURCES) && x.getKey().equals(TOOLS_PASS_KEY_IN_RESOURCES)).findAny();
            pwd = resourceDetails.orElseGet(() -> {
                ResourceDetails details = new ResourceDetails();
                details.setValue("no pass");
                return details;
            }).getValue();
        } catch (Throwable t) {
            return pwd;
        }
        String userName = TOOLS_USER;
        String auth = " ";
        try {
            auth = Base64.getEncoder().encodeToString((userName + ":" + pwd).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
        }
        return auth;
    }
}
