package com.poc.saml.configuration.authentication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider.ResponseToken;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;

public class CustomResponseAuthenticationConverter implements Converter<OpenSaml4AuthenticationProvider.ResponseToken, Saml2Authentication> {

	private Converter<ResponseToken, Saml2Authentication> delegate;

	public CustomResponseAuthenticationConverter() {
		delegate = OpenSaml4AuthenticationProvider.createDefaultResponseAuthenticationConverter();
	}

	@Override
	public Saml2Authentication convert(ResponseToken responseToken) {
		Saml2Authentication authentication = delegate.convert(responseToken);
		Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) authentication.getPrincipal();
		List<String> groups = principal.getAttribute("groups");
		Set<GrantedAuthority> authorities = new HashSet<>();
		if (groups != null) {
			groups.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);
		} else {
			authorities.addAll(authentication.getAuthorities());
		}
		return new Saml2Authentication(principal, authentication.getSaml2Response(), authorities);
	}

}
