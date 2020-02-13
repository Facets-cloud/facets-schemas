package com.capillary.ops.deployer;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.alturkovic.lock.redis.impl.SimpleRedisLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.ecr.EcrClient;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Deployer {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .genericModelSubstitutes(ResponseEntity.class);
    }

    @Bean(name = "codeBuildClient")
    public CodeBuildClient codeBuildClient() throws Exception {
        CodeBuildClient codeBuildClient =
            CodeBuildClient.builder()
                .region(Region.US_WEST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return codeBuildClient;
    }

    @Bean(name = "cloudWatchLogsClient")
    public CloudWatchLogsClient cloudWatchLogsClient() throws Exception {
        CloudWatchLogsClient cloudWatchClient =
            CloudWatchLogsClient.builder()
                .region(Region.US_WEST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        return cloudWatchClient;
    }

    @Bean
    public EcrClient getEcrClient() {
        return EcrClient.builder().region(Region.US_WEST_1).build();
    }

    @Bean
    public AmazonS3 getAmazonS3Client() {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
        return connectionFactory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    public SimpleRedisLock prodRedisLock(final StringRedisTemplate stringRedisTemplate) {
        return new SimpleRedisLock(stringRedisTemplate);
    }

    @Bean(name = "ECRChinaSyncPool")
    public ExecutorService executorServicePool() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        return pool;
    }
}
