package com.capillary.ops.deployer.security;

import com.capillary.ops.deployer.service.BasicUserDetailsService;
import com.capillary.ops.deployer.service.OAuth2UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;

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

  @Autowired
  private BasicUserDetailsService basicUserDetailsService;

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
            .antMatchers("/api/**", "/capillarycloud/api/**", "/cc-ui/**", "/tunnel/**")
            .authenticated()
            .and()
            .authorizeRequests()
            .antMatchers("/login*", "/pages/signin").permitAll()
            .antMatchers("/resources/static/**").permitAll()
            .antMatchers("/*.js").permitAll()
            .antMatchers("/*.css").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/pages/signin")
            .failureHandler(new ForwardAuthenticationFailureHandler("/pages/signin"))
            .failureForwardUrl("/pages/signin")
            .loginProcessingUrl("/perform_login")
            .successHandler(new RefererRedirectionAuthenticationSuccessHandler())
            .and()
            .logout()
            .logoutUrl("/perform_logout")
            .deleteCookies("JSESSIONID")
            .and()
            .oauth2Login()
            .successHandler(new RefererRedirectionAuthenticationSuccessHandler())
            .userInfoEndpoint()
            .userService(oAuth2UserService)
            .and()
            .and()
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(
                    (a,b,c) -> b.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            )
            .and()
            .cors();
  }
}