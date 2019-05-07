package com.capillary.ops.deployer;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.codebuild.CodeBuildClient;
import software.amazon.awssdk.services.ecr.EcrClient;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@SpringBootApplication(exclude = {EmbeddedMongoAutoConfiguration.class})
@EnableSwagger2
@EnableAsync
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class App {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(App.class, args);
  }

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

  @Configuration
  @Profile("dev")
  public static class EmbeddedMongoConfiguration extends EmbeddedMongoAutoConfiguration {

    public EmbeddedMongoConfiguration(MongoProperties properties, EmbeddedMongoProperties embeddedProperties, ApplicationContext context, IRuntimeConfig runtimeConfig) {
      super(properties, embeddedProperties, context, runtimeConfig);
    }
  }

}
