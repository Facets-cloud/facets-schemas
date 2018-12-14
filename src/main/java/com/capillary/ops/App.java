package com.capillary.ops;

import com.capillary.ops.service.DeisApiErrorHandler;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableSwagger2
@EnableAsync
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

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DeisApiErrorHandler());
        return restTemplate;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
        // Authentication : MongoUser --> Roles
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.inMemoryAuthentication()
                    .passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
                    .withUser(System.getenv().get("APP_USER"))
                    .password(System.getenv().get("APP_PASSWORD"))
                    .roles("ADMIN");
        }

        // Authorization : Role -> Access
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().and().authorizeRequests().antMatchers("/**").hasRole("ADMIN").and()
                    .csrf().disable().headers().frameOptions().disable();
        }

    }

    @Bean(name = "HelmChartConfig")
    public Map<String, LinkedTreeMap> helmChartConfig() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader helmConfigReader = new JsonReader(new FileReader(ResourceUtils.getFile("classpath:HelmChartConfigs.json")));
        Type type = new TypeToken<Map<String, LinkedTreeMap>>(){}.getType();
        Map<String, LinkedTreeMap> helmConfigMap = gson.fromJson(helmConfigReader, type);

        return helmConfigMap;
    }
}
