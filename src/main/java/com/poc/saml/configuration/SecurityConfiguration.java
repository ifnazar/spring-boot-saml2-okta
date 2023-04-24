package com.poc.saml.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import com.poc.saml.configuration.authentication.CustomResponseAuthenticationConverter;
import com.poc.saml.configuration.authorization.CustomAuthorizationManager;

@Configuration
public class SecurityConfiguration {

	@Bean
	public AuthenticationManager authenticationManager() {
		OpenSaml4AuthenticationProvider authenticationProvider = new OpenSaml4AuthenticationProvider();
		CustomResponseAuthenticationConverter responseAuthenticationConverter = new CustomResponseAuthenticationConverter();
		authenticationProvider.setResponseAuthenticationConverter(responseAuthenticationConverter);

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
		return new CustomAuthorizationManager<RequestAuthorizationContext>();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		// @formatter:off
		Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer = authorize -> {
			authorize
				.antMatchers("/*").authenticated()
				.antMatchers("/foo/coffee").permitAll()
				.anyRequest().access(authorizationManager());
		};
		http
			.authorizeHttpRequests(authorizeHttpRequestsCustomizer)
			.saml2Login(saml2 -> { saml2.authenticationManager(authenticationManager()); })
			.saml2Logout(withDefaults());
		// @formatter:om
		return http.build();
	}

}