package com.poc.saml.configuration.authorization;

import java.util.function.Supplier;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;

public class CustomAuthorizationManager<T> implements AuthorizationManager<T> {

	private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
		boolean granted = isGranted(authentication.get());
		return new AuthorizationDecision(granted);
	}

	private boolean isGranted(Authentication authentication) {
		return authentication != null && isNotAnonymous(authentication) && authentication.isAuthenticated();
	}

	private boolean isNotAnonymous(Authentication authentication) {
		return !this.trustResolver.isAnonymous(authentication);
	}

}
