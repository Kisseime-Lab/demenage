package com.startup.demenage.utils;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import java.util.function.Supplier;


import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {
        // String path = request.getRequestURI();
        Authentication auth = authentication.get();

        // Logique personnalisée pour décider si l'accès est autorisé
        boolean isAuthorized = auth.isAuthenticated();

        return new AuthorizationDecision(isAuthorized);
    }
}
