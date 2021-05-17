package com.capillary.ops.cp.helpers;

import com.capillary.ops.cp.bo.ClusterVariable;
import com.capillary.ops.cp.bo.CpuSize;
import com.capillary.ops.cp.bo.CpuUnits;
import com.capillary.ops.cp.bo.MemorySize;
import com.capillary.ops.cp.bo.MemoryUnits;
import com.capillary.ops.cp.bo.PVC;
import com.capillary.ops.cp.bo.PodSize;
import com.capillary.ops.cp.bo.StackIngressRule;
import com.capillary.ops.cp.bo.InstancePort;
import com.capillary.ops.cp.bo.Scaling;
import com.capillary.ops.cp.bo.StackProbe;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@TestPropertySource(locations = "classpath:test.properties")
public class StackTestUtils {

    @Value("${stack.name}")
    private String STACK_NAME;

    private String STACK_ROOT = "../../capillary-cloud-tf/stacks/";

    public JsonObject readFile(String filePath) throws FileNotFoundException {
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        return parser.parse(bufferedReader).getAsJsonObject();
    }

    public JsonObject getInstance(String moduleName, String instanceName) throws FileNotFoundException {
        String instancesPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/instances/" + instanceName + ".json";
        return readFile(instancesPath);
    }

    public PodSize getInstanceSizingWithUnits(String moduleName, String instanceName, String cpuUnits, String memoryUnits) throws Exception {
        JsonObject instance = getInstance(moduleName, instanceName);
        return getInstanceSizingWithUnits(moduleName, instance, cpuUnits, memoryUnits);
    }

    public PodSize getInstanceSizingWithUnits(String moduleName, JsonObject instance, String cpuUnits, String memoryUnits) throws Exception {
        String instanceSize = instance.get("size").getAsString();
        String instanceSizeStrategy = instance.get("resourceAllocationStrategy") == null ? null : instance.get("resourceAllocationStrategy").getAsString();

        String sizingMetaPath = "";
        if (instanceSizeStrategy == null || instanceSizeStrategy.isEmpty()) {
            sizingMetaPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/" + "sizing." + "json";
        } else {
            sizingMetaPath = STACK_ROOT + STACK_NAME + "/" + moduleName + "/" + "sizing." + instanceSizeStrategy + ".json";
        }
        JsonObject sizingJsonObject = readFile(sizingMetaPath).getAsJsonObject(instanceSize);
        Double cpu = Double.parseDouble(sizingJsonObject.get("podCPULimit").toString());
        CpuSize cpuSize = cpuUnits.equals("m") ? new CpuSize(cpu, CpuUnits.MilliCores) : new CpuSize(cpu, CpuUnits.Cores);

        Double memory = Double.parseDouble(sizingJsonObject.get("podMemoryLimit").toString());
        MemorySize memorySize = memoryUnits.equals("Mi") ? new MemorySize(memory, MemoryUnits.MegaBytes) : new MemorySize(memory, MemoryUnits.GigaBytes);

        return new PodSize(cpuSize, memorySize);
    }

    public HashMap<String, String> getStackVars() throws Exception {
        String stackDefinitionFile = STACK_ROOT + STACK_NAME + "/stack.json";
        JsonObject stackVarsObject = readFile(stackDefinitionFile).getAsJsonObject("stackVariables");
        return new Gson().fromJson(stackVarsObject.toString(), HashMap.class);
    }

    public Set<String> getClusterVars() throws Exception {
        String stackDefinitionFile = STACK_ROOT + STACK_NAME + "/stack.json";
        JsonObject clusterVarsObject = readFile(stackDefinitionFile).getAsJsonObject("clusterVariablesMeta");
        Type clusterVarsType = new TypeToken<Map<String, ClusterVariable>>() {
        }.getType();
        Map<String, ClusterVariable> envMap = new Gson().fromJson(clusterVarsObject.toString(), clusterVarsType);
        return envMap.entrySet()
                .stream()
                .filter(v -> v.getValue().getSecret() != null)
                .filter(v -> !v.getValue().getSecret())
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()))
                .keySet();
    }

    public HashMap<String,String> getInstanceEnvVariables(String moduleName, String instanceName) throws Exception {
        HashMap<String, String> envMap = new HashMap<>();
        JsonObject instance = getInstance(moduleName, instanceName);
        JsonObject staticVars = instance.getAsJsonObject("environmentVariables").getAsJsonObject("static");
        JsonObject dynamicVars = instance.getAsJsonObject("environmentVariables").getAsJsonObject("dynamic");
        envMap.putAll(new Gson().fromJson(staticVars.toString(), HashMap.class));
        envMap.putAll(new Gson().fromJson(dynamicVars.toString(), HashMap.class));
        return envMap;
    }

    public Set<String> getEnvsFromCredential(String moduleName, String instanceName) throws Exception {
        Set<String> envVars = new HashSet<>();
        JsonObject instance = getInstance(moduleName, instanceName);
        JsonArray envArray = new JsonArray();
        JsonArray mysqlArray = instance.getAsJsonObject("credentialRequests")
                .getAsJsonObject("dbs")
                .getAsJsonArray("mysql");

        JsonArray mongoArray = instance.getAsJsonObject("credentialRequests")
                .getAsJsonObject("dbs")
                .getAsJsonArray("mongo");


        JsonArray rmqArray = instance.getAsJsonObject("credentialRequests")
                .getAsJsonObject("queues")
                .getAsJsonArray("rabbitmq");

        envVars.addAll(getEnvsFromResourceJsonArray(mysqlArray));
        envVars.addAll(getEnvsFromResourceJsonArray(mongoArray));
        envVars.addAll(getEnvsFromResourceJsonArray(rmqArray));

        return envVars;
    }

    public Set<String> getEnvsFromResourceJsonArray(JsonArray credentialReqArrayOfResource){
        Set<String> envVars = new HashSet<>();
        for(JsonElement ele : credentialReqArrayOfResource){
            JsonArray envVarArray = ele.getAsJsonObject().getAsJsonArray("environmentVariables");
            for(JsonElement env: envVarArray){
                envVars.add(env.getAsJsonObject().get("userName").getAsString());
                envVars.add(env.getAsJsonObject().get("password").getAsString());
            }
        }
        return envVars;
    }

    private Map<String, JsonObject> getInstances(String resourceType) {
        String instancePath = STACK_ROOT + STACK_NAME + "/" + resourceType + "/instances";
        Map<String, JsonObject> instances = new HashMap<>();

        try (Stream<Path> paths = Files.list(Paths.get(instancePath))){
            Map<String, Optional<JsonObject>> optionalMap = new HashMap<>();
            paths.forEach(x -> {
                try {
                    optionalMap.put(x.getFileName().toString().split("\\.")[0], Optional.of(new JsonParser().parse(new FileReader(instancePath + "/" + x.getFileName().toString())).getAsJsonObject()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            optionalMap.forEach((key, value) -> value.ifPresent(y -> instances.put(key, y)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instances;
    }

    public Map<String, JsonObject> sampleInstances(Map<String, JsonObject> instances, List<Predicate<JsonObject>> predicates){
        HashMap<String, JsonObject> map = new HashMap<>();
        predicates.forEach(predicate -> {
            Map.Entry<String, JsonObject> jsonObjectEntry = instances.entrySet().stream().filter(x -> predicate.test(x.getValue())).findFirst().orElseThrow(() -> new RuntimeException("Couldn't find a single instance with the given predicate " + predicate.toString()));
            map.put(jsonObjectEntry.getKey(), jsonObjectEntry.getValue());
        });
        return map;
    }

    public Map<String, JsonObject> sampleInstances(String resourceType) {
        Map<String, JsonObject> instances = getInstances(resourceType);
        Random random = new Random();
        String randInstance = new ArrayList<>(instances.keySet()).get(random.nextInt(instances.size()));
        return new HashMap<String, JsonObject>(){{put("emf-mongo-cleanup-cronjob", instances.get("emf-mongo-cleanup-cronjob"));}};
    }

    public String getK8sInstanceSchedule(JsonObject instance) {
        return instance.get("schedule").getAsString();
    }

    public List<InstancePort> getInstancePorts(JsonObject instance) {
        List<InstancePort> instancePorts = new ArrayList<>();

        instance.get("ports").getAsJsonArray().forEach(x -> {
            instancePorts.add(new Gson().fromJson(x.getAsJsonObject(), InstancePort.class));
        });

        return instancePorts;
    }

    public Optional<StackProbe> getStackProbe(JsonObject instance, String probeType) {
        StackProbe probe = null;
        if (instance.get(probeType).getAsJsonObject().size() != 0){
            try {
                probe = new ObjectMapper().readValue(instance.get(probeType).toString(), StackProbe.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<StackProbe>> constraintViolations = validator.validate(probe);
            constraintViolations.stream().findFirst().ifPresent(error -> {
                throw new RuntimeException(error.getRootBeanClass().getName() + ", property: " + error.getPropertyPath() + "Error: " + error.getMessage());
            });
        }
        return Optional.ofNullable(probe);
    }

    public Scaling getInstanceScaling(JsonObject instance) {
        return new Gson().fromJson(instance.get("scaling").getAsJsonObject(), Scaling.class);
    }
    
    public Map<String, StackIngressRule> getIngressRules(JsonObject instance, String appName, String clusterName) throws FileNotFoundException {
        JsonArray ingressRules = Optional.ofNullable(instance.get("ingress_rules")).orElseGet(() -> new Gson().fromJson(new Gson().toJson(new ArrayList<>()), JsonElement.class)).getAsJsonArray();
        JsonElement elbIdleTimeoutSeconds = instance.get("elbIdleTimeoutSeconds");

        return createStackIngressRules(ingressRules, appName, clusterName, elbIdleTimeoutSeconds);
    }

    private Map<String, StackIngressRule> createStackIngressRules(JsonArray ingressRules, String appName, String clusterName, JsonElement elbIdleTimeout) throws FileNotFoundException {
        Map<String, StackIngressRule> ingressRuleMap = new HashMap<>();
        for (int i = 0; i < ingressRules.size(); i++){
            JsonObject currentIngressRule = ingressRules.get(i).getAsJsonObject();
            StackIngressRule stackIngressRule = createIngressRule(appName, i, elbIdleTimeout, currentIngressRule);

            JsonObject ingressClassInstance = getInstance("ingress", stackIngressRule.getIngressClass());
            String domainPrefix = currentIngressRule.get("domainPrefix").getAsString();

            stackIngressRule.setBasicAuthEnabled(Optional.ofNullable(ingressClassInstance.get("basic_auth_enabled")).orElse(new Gson().fromJson(new Gson().toJson(false), JsonElement.class)).getAsBoolean());
            stackIngressRule.setHost(domainPrefix + clusterName + "." + ingressClassInstance.get("base_domain").getAsString());

            ingressRuleMap.put(stackIngressRule.getIngressName(), stackIngressRule);

            Optional<JsonElement> domainsWithPrefix = getAdditionalDomainsWithPrefix(ingressClassInstance, domainPrefix);
            domainsWithPrefix.ifPresent(domains -> {
                JsonArray domainList = domains.getAsJsonArray();
                insertAdditionalDomainIngressRules(domainList, ingressRuleMap, stackIngressRule);
            });
        }
        return ingressRuleMap;
    }

    private StackIngressRule createIngressRule(String appName, Integer index, JsonElement elbIdleTimeout, JsonObject currentIngressRule) {
        StackIngressRule stackIngressRule = new StackIngressRule();
        stackIngressRule.setIngressName(appName + "-" + index);
        stackIngressRule.setK8sService(Optional.ofNullable(currentIngressRule.get("k8s_service")).orElse(new Gson().fromJson(appName, JsonElement.class)).getAsString());
        stackIngressRule.setTimeout(Optional.ofNullable(currentIngressRule.get("timeout")).orElse(elbIdleTimeout).getAsInt());
        stackIngressRule.setIngressClass(currentIngressRule.get("ingress").getAsString());
        stackIngressRule.setPath(currentIngressRule.get("path").getAsString());
        stackIngressRule.setTargetPort(currentIngressRule.get("targetPort").getAsInt());
        return stackIngressRule;
    }

    private Optional<JsonElement> getAdditionalDomainsWithPrefix(JsonObject ingressClassInstance, String domainPrefix) {
        JsonObject additionalDomains = Optional.ofNullable(ingressClassInstance.get("additional_domains")).orElse(new Gson().fromJson("{}", JsonElement.class)).getAsJsonObject();
        return Optional.ofNullable(additionalDomains.get(domainPrefix));
    }

    private void insertAdditionalDomainIngressRules(JsonArray domainList, Map<String, StackIngressRule> ingressRuleMap, StackIngressRule stackIngressRule) {
        for (int j = 0; j < domainList.size(); j++){
            StackIngressRule additionalStackIngressRule = new StackIngressRule(stackIngressRule);
            additionalStackIngressRule.setIngressName(stackIngressRule.getIngressName() + "-" + j);
            additionalStackIngressRule.setHost(domainList.get(j).getAsJsonObject().get("domain").getAsString());
            ingressRuleMap.put(additionalStackIngressRule.getIngressName(), additionalStackIngressRule);
        }
    }

    public List<PVC> getInstancePVCs(JsonObject instance) {
        List<PVC> pvcs = new ArrayList<>();

        instance.get("persistentVolumeClaims").getAsJsonArray().forEach(pvc -> {
            try {
                pvcs.add(new ObjectMapper().readValue(pvc.toString(), PVC.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return pvcs;
    }
}
