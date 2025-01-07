package com.startup.demenage.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

public class CustomPreAuthenticatedUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final UserDetailsService userDetailsService;

    public CustomPreAuthenticatedUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String authToken = (String) token.getPrincipal();
        // Validate the token and fetch user details
        if (isValidToken(authToken)) {
            return userDetailsService.loadUserByUsername(authToken);
        } else {
            throw new BadCredentialsException("Invalid token");
        }
    }

    private boolean isValidToken(String authToken) {
        // Implement token validation logic
        return true; // Placeholder for actual validation
    }

    private String extractUsernameFromToken(String authToken) {
        // Implement logic to extract username from token
        return "username"; // Placeholder for actual extraction
    }
}
