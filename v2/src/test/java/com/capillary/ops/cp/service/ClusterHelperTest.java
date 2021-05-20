package com.capillary.ops.cp.service;

import com.amazonaws.regions.Regions;
import com.capillary.ops.cp.bo.AwsCluster;
import com.capillary.ops.cp.bo.Stack;
import com.capillary.ops.cp.bo.StackFile;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ClusterHelperTest {

    @Tested
    ClusterHelper clusterHelper;

    @Injectable
    ClusterResourceRefreshService clusterResourceRefreshService;

    @Test
    public void getCommonVariables() {
        Map<String, String> stackVars = new HashMap<>();
        stackVars.put("Test1", "Val1");
        Stack crm = new Stack();
        crm.setStackVars(stackVars);
        Map<String, StackFile.VariableDetails> setClusterVariables = new HashMap<>();
        setClusterVariables.put("secret1", new StackFile.VariableDetails(true, null));
        setClusterVariables.put("envVar1", new StackFile.VariableDetails(false, "envVal"));
        crm.setClusterVariablesMeta(setClusterVariables);

        Map<String, String> inputVars = new HashMap<>();
        inputVars.put("secret1", "secret1Val1");
        String clusterName = "Test";
        String stack = "crm";
        TimeZone tz = TimeZone.getDefault();
        Regions region = Regions.US_EAST_1;
        AwsCluster cluster = new AwsCluster(clusterName);
        cluster.setStackName(stack);
        cluster.setAwsRegion(region.getName());
        cluster.setTz(tz);
        cluster.setUserInputVars(inputVars);

        Map<String, String> commonEnvironmentVariables = clusterHelper.getCommonVariables(cluster, crm);

        assert commonEnvironmentVariables.containsKey("TZ") && commonEnvironmentVariables.get("TZ").equals(tz.getID());
        System.out.println(tz.getID());
        assert commonEnvironmentVariables.containsKey("AWS_REGION") && commonEnvironmentVariables.get("AWS_REGION")
            .equals(region.getName());
        assert commonEnvironmentVariables.containsKey("CLUSTER") && commonEnvironmentVariables.get("CLUSTER")
            .equals(clusterName);
        assert
            commonEnvironmentVariables.containsKey("Test1") && commonEnvironmentVariables.get("Test1").equals("Val1");
        assert commonEnvironmentVariables.containsKey("envVar1") && commonEnvironmentVariables.get("envVar1")
            .equals("envVal");
    }

    @Test
    public void getSecrets() {
        Map<String, String> stackVars = new HashMap<>();
        stackVars.put("Test1", "Val1");
        Stack crm = new Stack();
        crm.setStackVars(stackVars);
        Map<String, StackFile.VariableDetails> setClusterVariables = new HashMap<>();
        setClusterVariables.put("secret1", new StackFile.VariableDetails(true, null));
        setClusterVariables.put("envVar1", new StackFile.VariableDetails(false, "envVal"));
        crm.setClusterVariablesMeta(setClusterVariables);

        Map<String, String> inputVars = new HashMap<>();
        inputVars.put("secret1", "secret1Val1");

        AwsCluster cluster = new AwsCluster("crm1");
        cluster.setUserInputVars(inputVars);

        Map<String, String> secrets = clusterHelper.getSecrets(cluster, crm);

        assert secrets.size() == 1;
    }

    @Test
    public void validateClusterVars() {
        Map<String, String> stackVars = new HashMap<>();
        stackVars.put("Test1", "Val1");
        Stack crm = new Stack();
        crm.setStackVars(stackVars);
        Map<String, StackFile.VariableDetails> setClusterVariables = new HashMap<>();
        setClusterVariables.put("secret1", new StackFile.VariableDetails(true, null));
        setClusterVariables.put("envVar1", new StackFile.VariableDetails(false, "envVal"));
        crm.setClusterVariablesMeta(setClusterVariables);

        Map<String, String> inputVars = new HashMap<>();
        inputVars.put("secret1", "secret1Val1");

        Map<String, String> validatedInput = clusterHelper.validateClusterVars(inputVars, crm);
        System.out.println(validatedInput);
        assert validatedInput.size() == 1;

    }

    @Test(expected = IllegalArgumentException.class)
    public void validateClusterVarsInvalidInput() {
        Map<String, String> stackVars = new HashMap<>();
        stackVars.put("Test1", "Val1");
        Stack crm = new Stack();
        crm.setStackVars(stackVars);
        Map<String, StackFile.VariableDetails> setClusterVariables = new HashMap<>();
        setClusterVariables.put("secret1", new StackFile.VariableDetails(true, null));
        setClusterVariables.put("envVar1", new StackFile.VariableDetails(false, "envVal"));
        crm.setClusterVariablesMeta(setClusterVariables);

        Map<String, String> inputVars = new HashMap<>();
        inputVars.put("secret1", "secret1Val1");
        inputVars.put("abc", "secret1Val1");

        Map<String, String> validatedInput = clusterHelper.validateClusterVars(inputVars, crm);
        System.out.println(validatedInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateClusterVarsIncompleteInput() {
        Map<String, String> stackVars = new HashMap<>();
        stackVars.put("Test1", "Val1");
        Stack crm = new Stack();
        crm.setStackVars(stackVars);
        Map<String, StackFile.VariableDetails> setClusterVariables = new HashMap<>();
        setClusterVariables.put("secret1", new StackFile.VariableDetails(true, null));
        setClusterVariables.put("envVar1", new StackFile.VariableDetails(false, "envVal"));
        crm.setClusterVariablesMeta(setClusterVariables);

        Map<String, String> inputVars = new HashMap<>();
        inputVars.put("envVar1", "envVar1Val1");

        Map<String, String> validatedInput = clusterHelper.validateClusterVars(inputVars, crm);
    }
}