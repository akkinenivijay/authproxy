package com.equifax;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author vijayakkineni
 */
@Configuration
public class SecurityConfiguration {

  @Bean
  SecurityFilterChain app(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .saml2Login(withDefaults())
        .saml2Logout(withDefaults());
    return http.build();
  }
}
