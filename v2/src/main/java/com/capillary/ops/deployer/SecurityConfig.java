package com.capillary.ops.deployer;

import com.capillary.ops.deployer.service.UserAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

  //@Autowired
  private UserAuthorizationService userAuthorizationService;

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
            .passwordEncoder(
                    org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
            .withUser(System.getenv().get("APP_USER"))
            .password(System.getenv().get("APP_PASSWORD"))
            .roles("ADMIN");
  }

  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/**")
            .hasRole("ADMIN")
            .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable();
  }
}
