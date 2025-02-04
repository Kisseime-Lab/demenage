package com.startup.demenage.provider;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.startup.demenage.domain.RoleEnum;

public class MyCustomPreAuthenticatedProvider implements AuthenticationProvider {

	private static final Log logger = LogFactory.getLog(MyCustomPreAuthenticatedProvider.class);
	private boolean throwExceptionWhenTokenRejected;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		logger.debug(LogMessage.format("PreAuthenticated authentication request: %s", authentication));
		if (authentication.getPrincipal() == null) {
			logger.debug("No pre-authenticated principal found in request.");
			if (this.throwExceptionWhenTokenRejected) {
				throw new BadCredentialsException("No pre-authenticated principal found in request.");
			}
			return null;
		}
		// UserDetails userDetails = this.preAuthenticatedUserDetailsService
		// .loadUserDetails((PreAuthenticatedAuthenticationToken) authentication);
		// this.userDetailsChecker.check(userDetails);
		PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(
				authentication.getPrincipal(),
				null, List.of(RoleEnum.CUSTOMER));
		result.setDetails(authentication.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
