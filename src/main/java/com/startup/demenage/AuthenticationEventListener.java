package com.startup.demenage;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {

    @EventListener
    public void handleSuccess(AuthenticationSuccessEvent event) {
        System.out.println("Authentication succeeded: " + event.getAuthentication().getName());
    }

    @EventListener
    public void handleFailure(AuthenticationFailureBadCredentialsEvent event) {
        System.out.println("Authentication failed: " + event.getException().getMessage());
    }
}