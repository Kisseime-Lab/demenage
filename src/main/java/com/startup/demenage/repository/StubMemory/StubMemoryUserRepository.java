package com.startup.demenage.repository.StubMemory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.repository.UserRepository;

// @Repository
// @Profile("test")
public class StubMemoryUserRepository implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(StubMemoryUserRepository.class);

    private Map<String, UserDomain> registry = new HashMap<>();

    @Override
    public Optional<UserDomain> findByUsername(String username) {
        logger.info("start findByUsername method");
        return this.registry.values().stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }

    @Override
    public UserDomain save(UserDomain user) {
        this.registry.putIfAbsent(user.getId(), user);
        return user;
    }

    @Override
    public void delete(UserDomain user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void deleteByUserId(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteByUserId'");
    }

    @Override
    public Optional<UserDomain> findById(String id) {
        return Optional.ofNullable(this.registry.get(id));
    }

}
