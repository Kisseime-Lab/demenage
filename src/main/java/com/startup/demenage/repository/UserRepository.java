package com.startup.demenage.repository;

import java.util.Optional;

import com.startup.demenage.domain.UserDomain;

public interface UserRepository {

    Optional<UserDomain> findByUsername(String username);

    UserDomain save(UserDomain user);

    void delete(UserDomain user);

    void deleteByUserId(String id);

    Optional<UserDomain> findById(String id);

}
