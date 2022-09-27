package com.equifax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.servlet.filter.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author vijayakkineni
 */
@Configuration
public class SecurityConfiguration {

  @Autowired
  private RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

  @Bean
  SecurityFilterChain app(HttpSecurity http) throws Exception {

    RelyingPartyRegistrationResolver relyingPartyRegistrationResolver =
            new DefaultRelyingPartyRegistrationResolver(this.relyingPartyRegistrationRepository);

    Saml2MetadataFilter filter = new Saml2MetadataFilter(
            relyingPartyRegistrationResolver,
            new OpenSamlMetadataResolver());

    filter.setRequestMatcher(new AntPathRequestMatcher("/saml2/metadata/{registrationId}", "GET"));

    http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
            .saml2Login(withDefaults())
            .addFilterBefore(filter, Saml2WebSsoAuthenticationFilter.class)
            .saml2Logout(withDefaults());
    return http.build();
  }
}
