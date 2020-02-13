package com.capillary.ops.deployer.security;

import com.capillary.ops.deployer.service.OAuth2UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
  private InternalRequestAccessController internalRequestAccessController;

  protected void configure(HttpSecurity http) throws Exception {

    http
            .authorizeRequests()
            .antMatchers("/cc/**")
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
            .antMatchers("/api/**", "/capillarycloud/api/**")
            .authenticated()
            .and()
            .oauth2Login()
            .userInfoEndpoint()
            .userService(oAuth2UserService)
            .and()
            .and()
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(
                    (a,b,c) -> {b.sendError(HttpServletResponse.SC_UNAUTHORIZED);}
            )
            .and()
            .cors();
  }
}
