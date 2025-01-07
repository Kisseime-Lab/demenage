package com.startup.demenage.filters;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.http.HttpServletRequest;

public class FirebaseTokenFilter extends AbstractPreAuthenticatedProcessingFilter {

    // private final ApplicationEventPublisher eventPublisher;
    // private final AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    // private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public FirebaseTokenFilter() {
        // this.setAuthenticationManager(manager);
        // this.setApplicationEventPublisher(context);
        this.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler());
        // AuthenticationManagerBuilder authBuilder = context.getBean(AuthenticationManagerBuilder.class);
        // authBuilder.
        // this.setApplicationEventPublisher(aPublisher);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        // if (Objects.nonNull(request.getParameter("fromGoogle")) && !request.getMethod().equals("GET")) {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuthentication != null && currentAuthentication.isAuthenticated()) {
            return null; // Utilisateur déjà authentifié, ne rien faire
        }
        String token = extractToken(request);
        if (token != null && !Objects.equals(token, "")) {
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                // FirebaseAuthenticationToken authentication = new FirebaseAuthenticationToken(decodedToken);
                // SecurityContextHolder.getContext().setAuthentication(authentication);
                return decodedToken.getEmail(); // Le principal est l'UID de l'utilisateur Firebase
            } catch (FirebaseAuthException e) {
                SecurityContextHolder.clearContext();
            }
        }
        return null;
    }

    // @Override
    // protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	// 		Authentication authResult) throws IOException, ServletException {
	// 	this.logger.debug(LogMessage.format("Authentication success: %s", authResult));
    //     SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
	// 	SecurityContext context = securityContextHolderStrategy.createEmptyContext();
	// 	context.setAuthentication(authResult);
    //     // SecurityContextHolder.getContext().setAuthentication(authResult);
	// 	securityContextHolderStrategy.setContext(context);
	// 	securityContextRepository.saveContext(context, request, response);
	// 	if (this.eventPublisher != null) {
	// 		this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
	// 	}
	// 	if (this.authenticationSuccessHandler != null) {
	// 		this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
	// 	}
	// }
    
    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return ""; // No credentials needed here
    }

    

    // @Override
    // protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    //         throws IOException, ServletException {
    //     String token = (String) getPreAuthenticatedPrincipal(request);
    //     if (token != null) {
    //         try {
    //             FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
    //             FirebaseAuthenticationToken authentication = new FirebaseAuthenticationToken(decodedToken);
    //             SecurityContextHolder.getContext().setAuthentication(authentication);
    //         } catch (FirebaseAuthException e) {
    //             SecurityContextHolder.clearContext();
    //             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase Token");
    //             return;
    //         }
    //     }
    //     chain.doFilter(request, response);
    // }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("X-Custom-Token");
        return header;
        // if (header != null && header.startsWith("Bearer ")) {
        //     return header.substring(7); // Extrait le jeton
        // }
        // return null;
    }
}
