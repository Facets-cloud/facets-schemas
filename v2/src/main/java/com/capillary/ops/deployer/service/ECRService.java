package com.capillary.ops.deployer.service;

import com.capillary.ops.deployer.App;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.service.interfaces.IECRService;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecr.model.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Profile("!dev")
public class ECRService implements IECRService {

    @Autowired
    private EcrClient ecrClient;

    @Autowired
    private Environment environment;

    @Override
    public void createRepository(Application application) {
        String repositoryName = getRepositoryName(application);
        CreateRepositoryRequest createRepositoryRequest =
                CreateRepositoryRequest.builder().repositoryName(repositoryName).build();
        ecrClient.createRepository(createRepositoryRequest);
        setEcrPolicy(application);
    }

    @Override
    public List<String> listImages(Application application) {
        String repositoryName = getRepositoryName(application);
        List<String> images
                = ecrClient.describeImages(DescribeImagesRequest.builder()
                .repositoryName(repositoryName).maxResults(10).build())
                .imageDetails().stream()
                .filter(x-> x.imageTags() != null && ! x.imageTags().isEmpty())
                .sorted(Comparator.comparing(ImageDetail::imagePushedAt).reversed())
                .map(x -> environment.getProperty("ecr.registry") + "/" + repositoryName + ":" + x.imageTags().get(0))
                .collect(Collectors.toList());
        return images;
    }

    @Override
    public String findImageBetweenTimes(Application application, Instant from, Instant to) {
        String repositoryName = getRepositoryName(application);
        Optional<String> imageOptional = ecrClient.describeImages(DescribeImagesRequest.builder()
                .repositoryName(repositoryName).maxResults(1000).build())
                .imageDetails().stream()
                .filter(x -> x.imageTags() != null && !x.imageTags().isEmpty())
                .filter(x -> x.imagePushedAt().isAfter(from) && x.imagePushedAt().isBefore(to))
                .map(x -> environment.getProperty("ecr.registry") + "/" + repositoryName + ":" + x.imageTags().get(0))
                .findFirst();
        return imageOptional.isPresent() ? imageOptional.get() : null;
    }

    private String getRepositoryName(Application application) {
        return application.getApplicationFamily().name().toLowerCase() + "/" + application.getName();
    }

    private void setEcrPolicy(Application application) {
        SetRepositoryPolicyRequest policyRequest =
                SetRepositoryPolicyRequest.builder()
                        .repositoryName(getRepositoryName(application))
                        .policyText(getPolicy())
                        .build();

        ecrClient.setRepositoryPolicy(policyRequest);
    }

    private String getPolicy() {
        try {
            String template =
                    CharStreams.toString(
                            new InputStreamReader(
                                    App.class.getClassLoader().getResourceAsStream("aws/EcrPolicyTemplate.json"),
                                    Charsets.UTF_8));
            return template;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
