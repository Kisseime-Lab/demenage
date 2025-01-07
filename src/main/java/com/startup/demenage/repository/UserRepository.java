package com.startup.demenage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.startup.demenage.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
}
