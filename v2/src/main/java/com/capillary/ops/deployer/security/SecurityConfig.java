package com.capillary.ops.deployer.security;

import com.capillary.ops.deployer.service.BasicUserDetailsService;
import com.capillary.ops.deployer.service.OAuth2UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.GenericFilter;
import javax.servlet.http.HttpServletResponse;

@Profile("!apidev")
@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

  @Autowired
  private OAuth2UserServiceImpl oAuth2UserService;

  @Autowired
  protected GithubIpAccessController githubIpAccessController;

  @Autowired
  private BitbucketIpAccessController bitbucketIpAccessController;

  @Autowired
  private SonarCallbackAccessController sonarCallbackAccessController;

  @Autowired
  private InternalRequestAccessController internalRequestAccessController;

  @Autowired
  private BasicUserDetailsService basicUserDetailsService;


  @Value("${facets_only:false}")
  private Boolean isFacetsOnly;

  private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(basicUserDetailsService).passwordEncoder(passwordEncoder());
  }

  protected void configure(HttpSecurity http) throws Exception {
    http
            .headers()
            .frameOptions()
            .sameOrigin();
    http
            .authorizeRequests()
            .antMatchers("/cc/**")
            .access("@internalRequestAccessController.authenticate(request)")
            .and()
            .authorizeRequests()
            .antMatchers("/api/codebuild/builds/*/refresh")
            .access("@internalRequestAccessController.authenticate(request)")
            .and()
            .authorizeRequests()
            .antMatchers("/api/*/applications/*/webhooks/pr/github")
            .access("@githubIpAccessController.authenticate(request)")
            .and()
            .authorizeRequests()
            .antMatchers("/api/*/applications/*/webhooks/pr/bitbucket")
            .access("@bitbucketIpAccessController.authenticate(request)")
            .and()
            .authorizeRequests()
            .antMatchers("/callback/sonar")
            .access("@sonarCallbackAccessController.authenticate(request)")
            .and()
            .authorizeRequests()
            .antMatchers("/api/**", "/capillarycloud/api/**", "/cc-ui/**", "/tunnel/**")
            .authenticated();

      http.formLogin()
              .loginProcessingUrl("/perform_login")
              .successHandler(new SimpleUrlAuthenticationSuccessHandler())
              .failureHandler(new SimpleUrlAuthenticationFailureHandler());


    String defaultSuccessUrl = "/";
    if (isFacetsOnly) {
      defaultSuccessUrl = "/capc";
    }
    http.oauth2Login()
              .defaultSuccessUrl(defaultSuccessUrl)
              .userInfoEndpoint()
              .userService(oAuth2UserService);

      http.csrf().disable()
              .exceptionHandling()
              .authenticationEntryPoint(
                      (a, b, c) -> b.sendError(HttpServletResponse.SC_UNAUTHORIZED)
              )
              .and()
              .cors();

      http.logout()
              .logoutUrl("/perform_logout")
              .deleteCookies("JSESSIONID");
  }
}