package com.startup.demenage.domain;

import java.util.UUID;

public class UserTokenDomain {

    private UUID id = UUID.randomUUID();
    private String refreshToken;
    private UserDomain user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserDomain getUser() {
        return user;
    }

    public void setUser(UserDomain user) {
        this.user = user;
    }
}
