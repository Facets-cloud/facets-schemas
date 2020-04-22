package com.capillary.ops.cp.service;

import com.capillary.ops.cp.bo.AbstractCluster;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ClusterHelper {

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
                .map(e -> e.getKey()).collect(Collectors.toList());

        List<String> allValidKeys =
            stackClusterVariables.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());

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
        return stackDefinition.entrySet().stream().filter(predicate).collect(Collectors.toMap(Map.Entry::getKey, e -> {
            String clusterVal = clusterVars.get(e.getKey());
            String stackVal = e.getValue().getValue();
            return clusterVal == null ? stackVal : clusterVal;
        }));
    }
}
