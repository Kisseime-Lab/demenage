package com.startup.demenage.filters;

import java.io.IOException;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FirebaseTokenFilter2 extends OncePerRequestFilter {

    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private final AuthenticationManager authenticationManager;
    
    public FirebaseTokenFilter2(ApplicationEventPublisher eventPublisher,
            AuthenticationManager manager) {
        this.eventPublisher = eventPublisher;
        this.authenticationManager = manager;
    }

    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);
        if (token != null) {
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                // FirebaseAuthenticationToken authentication = new FirebaseAuthenticationToken(decodedToken);
                // SecurityContextHolder.getContext().setAuthentication(authentication);
                 // Le principal est l'UID de l'utilisateur Firebase
                
                PreAuthenticatedAuthenticationToken authenticationRequest = new PreAuthenticatedAuthenticationToken(
					decodedToken.getEmail(), null);
			    authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
			    Authentication authResult = this.authenticationManager.authenticate(authenticationRequest);
                SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
                SecurityContext context = securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authResult);
                // SecurityContextHolder.getContext().setAuthentication(authResult);
                securityContextHolderStrategy.setContext(context);
                securityContextRepository.saveContext(context, request, response);
                if (this.eventPublisher != null) {
			    this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
                }
                if (this.authenticationSuccessHandler != null) {
                    this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
                }
            } catch (FirebaseAuthException e) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Boolean.TRUE.equals(extractToken(request) == null);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("X-Custom-Token");
        return header;
        // if (header != null && header.startsWith("Bearer ")) {
        //     return header.substring(7); // Extrait le jeton
        // }
        // return null;
    }
}
