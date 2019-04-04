package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.App;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ApplicationFamily;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecr.model.CreateRepositoryRequest;
import software.amazon.awssdk.services.ecr.model.SetRepositoryPolicyRequest;

import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ECRService {

    @Autowired
    private EcrClient ecrClient;

    public void createRepository(Application application) {
        String repositoryName = getRepositoryName(application);
        CreateRepositoryRequest createRepositoryRequest =
                CreateRepositoryRequest.builder().repositoryName(repositoryName).build();
        ecrClient.createRepository(createRepositoryRequest);
        setEcrPolicy(application);
    }

    private String getRepositoryName(Application application) {
        return application.getApplicationFamily().name().toLowerCase() + "/" + application.getName();
    }

    private void setEcrPolicy(Application application) {
        SetRepositoryPolicyRequest policyRequest =
                SetRepositoryPolicyRequest.builder()
                        .repositoryName(getRepositoryName(application))
                        .policyText(getEcrPolicyForFamily(application.getApplicationFamily()))
                        .build();

        ecrClient.setRepositoryPolicy(policyRequest);
    }

    private String getEcrPolicyForFamily(ApplicationFamily applicationFamily) {
        try {
            String template =
                    CharStreams.toString(
                            new InputStreamReader(
                                    App.class.getClassLoader().getResourceAsStream("aws/EcrPolicyTemplate.json"),
                                    Charsets.UTF_8));
            return template.replace("{{ACCOUNT_ID}}", applicationFamily.getAwsAccountId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
