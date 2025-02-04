package com.startup.demenage.repository.Jpa.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "user_token")
public class UserTokenEntity {

    @Id
    private UUID id = UUID.randomUUID();
    @NotNull(message = "Refresh token is required.")
    @Basic(optional = false)
    @Column(name = "refresh_token")
    private String refreshToken;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
