package com.capillary.ops;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.alturkovic.lock.configuration.EnableDistributedLock;
import com.github.alturkovic.lock.redis.impl.SimpleRedisLock;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ForwardedHeaderFilter;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(exclude = {EmbeddedMongoAutoConfiguration.class})
@EnableSwagger2
@EnableAsync
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableScheduling
@EnableDistributedLock
@EnableCaching
@EnableZuulProxy
public class App {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }

  @Configuration
  @Profile("dev")
  public static class EmbeddedMongoConfiguration extends EmbeddedMongoAutoConfiguration {

    public EmbeddedMongoConfiguration(MongoProperties properties, EmbeddedMongoProperties embeddedProperties, ApplicationContext context, IRuntimeConfig runtimeConfig) {
      super(properties, embeddedProperties, context, runtimeConfig);
    }
  }

  @Profile("apidev")
  @Configuration
  public static class NoAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
              .anyRequest()
              .permitAll()
              .and()
              .csrf()
              .disable();
    }
  }

  @Bean
  FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {

    final FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<ForwardedHeaderFilter>();

    filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return filterRegistrationBean;
  }

  /**
   * Thread pool to execute Schedules
   *
   * @return
   */
  @Bean
  public TaskScheduler poolScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix("ReleaseScheduleService");
    scheduler.setPoolSize(3);
    scheduler.initialize();
    return scheduler;
  }

}
