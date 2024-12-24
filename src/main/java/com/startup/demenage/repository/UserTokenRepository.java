package com.startup.demenage.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.startup.demenage.entity.UserTokenEntity;

public interface UserTokenRepository extends
        CrudRepository<UserTokenEntity, String> {
    Optional<UserTokenEntity> findByRefreshToken
            (String refreshToken);
    Optional<UserTokenEntity> deleteByUserId(String userId);
}