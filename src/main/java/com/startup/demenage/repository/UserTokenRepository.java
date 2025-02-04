package com.startup.demenage.repository;

import java.util.Optional;

import com.startup.demenage.domain.UserTokenDomain;

public interface UserTokenRepository {
        Optional<UserTokenDomain> findByRefreshToken(String refreshToken);

        void deleteByUserId(String userId);

        void save(UserTokenDomain userTokenDomain);

        void delete(UserTokenDomain userTokenDomain);
}