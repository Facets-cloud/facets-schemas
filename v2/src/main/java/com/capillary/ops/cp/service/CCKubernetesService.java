package com.capillary.ops.cp.service;

import com.capillary.ops.App;
import com.capillary.ops.cp.bo.K8sCredentials;
import com.capillary.ops.cp.constants.CCRoles;
import com.capillary.ops.cp.repository.K8sCredentialsRepository;
import com.capillary.ops.utils.DeployerUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;
import com.samskivert.mustache.Mustache;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ServiceAccount;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.RoleBinding;
import io.fabric8.kubernetes.api.model.rbac.RoleRef;
import io.fabric8.kubernetes.api.model.rbac.Subject;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CCKubernetesService {

    @Autowired
    private K8sCredentialsRepository k8sCredentialsRepository;

    private KubernetesClient getKubernetesClient(String clusterId) {
        K8sCredentials credentials = k8sCredentialsRepository.findOneByClusterId(clusterId).get();
        return new DefaultKubernetesClient(
                new ConfigBuilder()
                        .withMasterUrl(credentials.getKubernetesApiEndpoint())
                        .withOauthToken(credentials.getKubernetesToken())
                        .withTrustCerts(true)
                        .build());
    }

    public String createUserServiceAccount(String clusterId) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterId);
        String saName = getSaName();

        ServiceAccount serviceAccount = kubernetesClient.serviceAccounts().inNamespace("default").withName(saName).get();

        if (serviceAccount == null) {
            kubernetesClient.serviceAccounts()
                    .createOrReplaceWithNew()
                    .withNewMetadata()
                    .withNamespace("default")
                    .withName(saName)
                    .withLabels(ImmutableMap.of("ccowned", "true"))
                    .endMetadata().done();
            serviceAccount = kubernetesClient.serviceAccounts().inNamespace("default").withName(saName).get();
        }

        this.attachRoles(clusterId);

        String tokenSecretName = serviceAccount.getSecrets().get(0).getName();
        Map<String, String> secretData = kubernetesClient.secrets().inNamespace("default")
                .withName(tokenSecretName).get().getData();
        String cacert = secretData.get("ca.crt");
        String token = new String(Base64.getDecoder().decode(secretData.get("token").getBytes()));
        K8sCredentials credentials = k8sCredentialsRepository.findOneByClusterId(clusterId).get();
        String kubeconfig = Mustache.compiler()
                .escapeHTML(false).compile(getKubeconfigTemplate())
                .execute(ImmutableMap.of("CACERT", cacert,
                "SERVER", credentials.getKubernetesApiEndpoint(),
                "TOKEN", token
        ));
        return kubeconfig;
    }

    public String getSaName() {
        return DeployerUtil.getUser().getName().split("@")[0];
    }

    public void attachRoles(String clusterId) {
        if (hasRole(CCRoles.CC_ADMIN)) {
            attachClusterRole(clusterId, "cluster-admin");
        }
        if (hasRole(CCRoles.K8S_DEBUGGER)) {
            attachRole(clusterId, "cc-debug-role");
        }
        if (hasRole(CCRoles.K8S_READER)) {
            attachRole(clusterId, "cc-read-role");
        }
    }

    private void attachRole(String clusterId, String roleName) {
        String saName = getSaName();
        KubernetesClient kubernetesClient = getKubernetesClient(clusterId);
        String roleBindingName = getRoleBindingName(roleName, saName);
        RoleBinding roleBinding =
                kubernetesClient.rbac().roleBindings().inNamespace("default")
                        .withName(roleBindingName).get();

        if (roleBinding == null) {
            kubernetesClient.rbac().roleBindings()
                    .createOrReplaceWithNew()
                    .withNewMetadata()
                    .withNamespace("default")
                    .withName(roleBindingName)
                    .withLabels(ImmutableMap.of("ccowned", "true"))
                    .endMetadata()
                    .withRoleRef(new RoleRef("rbac.authorization.k8s.io", "Role", roleName))
                    .withSubjects(new Subject("", "ServiceAccount", saName, "default"))
                    .done();
            roleBinding =
                    kubernetesClient.rbac().roleBindings().inNamespace("default")
                            .withName(roleBindingName).get();
        }
    }

    private void attachClusterRole(String clusterId, String roleName) {
        String saName = getSaName();
        KubernetesClient kubernetesClient = getKubernetesClient(clusterId);
        String roleBindingName = getClusterRoleBindingName(roleName, saName);
        ClusterRoleBinding roleBinding =
                kubernetesClient.rbac().clusterRoleBindings().inNamespace("default")
                        .withName(roleBindingName).get();

        if (roleBinding == null) {
            kubernetesClient.rbac().clusterRoleBindings()
                    .createOrReplaceWithNew()
                    .withNewMetadata()
                    .withNamespace("default")
                    .withName(roleBindingName)
                    .withLabels(ImmutableMap.of("ccowned", "true"))
                    .endMetadata()
                    .withRoleRef(new RoleRef("rbac.authorization.k8s.io", "ClusterRole", roleName))
                    .withSubjects(new Subject("", "ServiceAccount", saName, "default"))
                    .done();
            roleBinding =
                    kubernetesClient.rbac().clusterRoleBindings().inNamespace("default")
                            .withName(roleBindingName).get();
        }
    }

    private String getRoleBindingName(String roleName, String saName) {
        return saName + "-" + roleName + "-rb";
    }

    private String getClusterRoleBindingName(String roleName, String saName) {
        return saName + "-" + roleName + "-crb";
    }


    private String getKubeconfigTemplate() {
        try {
            String template =
                    CharStreams.toString(
                            new InputStreamReader(
                                    App.class.getClassLoader().getResourceAsStream("cc/kubeconfig.template"),
                                    Charsets.UTF_8));
            return template;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasRole(String role) {
        return DeployerUtil.getUser()
                .getAuthorities().stream().map(x -> ((GrantedAuthority) x)
                .getAuthority()).anyMatch(x -> x.equalsIgnoreCase("ROLE_" + role));
    }

    public void detachExpiredRoles(String clusterId) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterId);
        kubernetesClient.rbac().roleBindings()
                .inNamespace("default").withLabel("ccowned", "true")
                .list().getItems().stream()
                .filter(x -> isExpired(x))
                .forEach(x ->
                        kubernetesClient.rbac().roleBindings()
                                .inNamespace("default").withName(x.getMetadata().getName()).delete());
    }

    public void detachExpiredClusterRoles(String clusterId) {
        KubernetesClient kubernetesClient = getKubernetesClient(clusterId);
        kubernetesClient.rbac().clusterRoleBindings()
                .inNamespace("default").withLabel("ccowned", "true")
                .list().getItems().stream()
                .filter(x -> isExpired(x))
                .forEach(x ->
                        kubernetesClient.rbac().clusterRoleBindings()
                                .inNamespace("default").withName(x.getMetadata().getName()).delete());
    }

    private boolean isExpired(HasMetadata x) {
        boolean isExpired = new Date().getTime() - DatatypeConverter.parseDateTime(x.getMetadata().getCreationTimestamp()).getTime().getTime() > 24 * 3600 * 1000;
        return isExpired;
    }
}
