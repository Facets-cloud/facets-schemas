package com.capillary.ops.deployer.service;

import com.capillary.ops.App;
import com.capillary.ops.deployer.bo.Application;
import com.capillary.ops.deployer.bo.ECRRegistry;
import com.capillary.ops.deployer.bo.Registry;
import com.capillary.ops.deployer.service.interfaces.IECRService;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.ecr.model.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
@Profile("!dev")
public class ECRService implements IECRService {

    @Autowired
    private EcrClient ecrClient;

    @Autowired
    private Environment environment;

    @Qualifier("ECRChinaSyncPool")
    @Autowired
    private ExecutorService executorServicePool;

    @Autowired
    private RegistryService registryService;

    private static final Logger logger = LoggerFactory.getLogger(ECRService.class);

    @Override
    public void createRepository(Application application) {
        createRepository(ecrClient, application, "default", true);
        createAdditionalRepositories(application);
    }

    private void createAdditionalRepositories(Application application){
        List<Registry> additionalRegistries = registryService.getAdditionalRegistries(application);

        additionalRegistries.forEach(registry -> {
            ECRRegistry ecrRegistry = (ECRRegistry) registry;
            createRepository(getEcrClient(ecrRegistry), application, ecrRegistry.getAwsAccountId(), false);
        });
    }

    private void createRepository(EcrClient ecrClient, Application application, String awsAccountId, boolean setPolicy){
        String repositoryName = getRepositoryName(application);
        CreateRepositoryRequest createRepositoryRequest =
                CreateRepositoryRequest.builder().repositoryName(repositoryName).build();
        try {
            ecrClient.createRepository(createRepositoryRequest);
            if (setPolicy){
                setEcrPolicy(ecrClient, application);
            }
        } catch (RepositoryAlreadyExistsException e){
            logger.info("repository {} already exists in the account {}", repositoryName, awsAccountId);
        }
    }

    private EcrClient getEcrClient(ECRRegistry ecrRegistry){
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(AwsBasicCredentials.create(ecrRegistry.getAwsKey(), ecrRegistry.getAwsSecret()));
        return EcrClient.builder().credentialsProvider(provider).region(Region.of(ecrRegistry.getAwsRegion())).build();
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
    public String findImageBetweenTimes(Application application, Instant from, Instant to, String gitVersion) {
        String shortGitVersion = gitVersion.substring(0,7);
        String repositoryName = getRepositoryName(application);
        Optional<String> imageOptional = ecrClient.describeImages(DescribeImagesRequest.builder()
                .repositoryName(repositoryName).maxResults(1000).build())
                .imageDetails().stream()
                .filter(x -> x.imageTags() != null && !x.imageTags().isEmpty())
                .filter(x -> x.imagePushedAt().isAfter(from) && x.imagePushedAt().isBefore(to))
                .filter(x -> x.imageTags().get(0).contains(shortGitVersion))
                .map(x -> environment.getProperty("ecr.registry") + "/" + repositoryName + ":" + x.imageTags().get(0))
                .findFirst();
        return imageOptional.orElse(null);
    }

    @Override
    public void syncToChinaECR(String imageURL) {
        String baseURL = "http://dregsync.capillary.in/sync?id=";
        String finalURL = baseURL + imageURL;
        executorServicePool.submit(
                () -> {
                    try {
                        makeRequest(finalURL);
                    } catch (IOException e) {
                        logger.error("Error with China ECR sync {}", e);
                    }
                });
    }

    @Override
    public void deleteRepository(Application application) {
        String repositoryName = getRepositoryName(application);
        ecrClient.deleteRepository(DeleteRepositoryRequest.builder().repositoryName(repositoryName).build());
    }

    private CloseableHttpClient getGETClient() {
        return HttpClients.custom().build();
    }

    private void makeRequest(String requestUri) throws IOException {
        CloseableHttpClient httpClient = this.getGETClient();
        HttpUriRequest request = RequestBuilder.get()
                .setUri(requestUri)
                .build();
        CloseableHttpResponse httpResponse = httpClient.execute(request);
        httpClient.close();
        logger.info("Sync request sent - {}",requestUri);
    }

    private String getRepositoryName(Application application) {
        return application.getApplicationFamily().name().toLowerCase() + "/" + application.getName();
    }

    private void setEcrPolicy(EcrClient ecrClient, Application application) {
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
