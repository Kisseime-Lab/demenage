package com.startup.demenage.utils;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.google.firebase.auth.FirebaseToken;
import com.startup.demenage.entity.RoleEnum;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {

    private final FirebaseToken firebaseToken;
    private final Object principal;


    public FirebaseAuthenticationToken(FirebaseToken firebaseToken) {
        super(List.of(RoleEnum.CUSTOMER));
        this.firebaseToken = firebaseToken;
        this.principal = firebaseToken.getUid();
        setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return firebaseToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
    
}
