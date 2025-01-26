package com.startup.demenage.service.serviceImpl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.startup.demenage.entity.RoleEnum;
import com.startup.demenage.entity.UserEntity;
import com.startup.demenage.entity.UserTokenEntity;
import com.startup.demenage.model.InputPassword;
import com.startup.demenage.model.PasswordValidated;
import com.startup.demenage.model.RefreshToken;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;
import com.startup.demenage.repository.UserRepository;
import com.startup.demenage.repository.UserTokenRepository;
import com.startup.demenage.security.JwtManager;
import com.startup.demenage.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserTokenRepository userTokenRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtManager tokenManager;

    public UserServiceImpl(
            UserRepository repository,
            UserTokenRepository userTokenRepository,
            PasswordEncoder bCryptPasswordEncoder,
            JwtManager tokenManager) {
        this.repository = repository;
        this.userTokenRepository = userTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenManager = tokenManager;
    }

    @Override
    public UserEntity getUserById(String id, String byAdmin) {
        UserEntity _user = repository.findById(id).orElse(null);
        if (_user == null) {
            throw new UsernameNotFoundException("Invalid user.");
        }
        if (_user.isDeleted() && Objects.isNull(byAdmin)) {
            throw new UsernameNotFoundException("Invalid user.");
        }
        return _user;
    }

    @Override
    public Optional<SignedInUser> createUser(User user) {
        UserEntity _user = this.findUserByEmail(user.getUsername(), null);
        if (_user != null) {
            throw new RuntimeException("Use different username and email.");
        }
        UserEntity userEntity = repository.save(toEntity(user));
        return Optional.of(createSignedUserWithRefreshToken(userEntity));
    }

    @Override
    public UserEntity updateUser(User user, String email) {
        UserEntity existingUserEntity = this.findUserByEmail(email, null);
        UserEntity userEntity = this.toEntity(user);
        if (!Objects.equals(existingUserEntity.isDeleted(), userEntity.isDeleted())) {
            userEntity.setDeleted(true);
            userEntity.setDeletedAt(LocalDateTime.now().toString());
        }
        if (Objects.isNull(userEntity.getPassword())) {
            userEntity.setPassword(existingUserEntity.getPassword());
        }
        userEntity.setId(existingUserEntity.getId());
        return repository.save(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(String email, String byAdmin) {
        UserEntity userEntity = this.findUserByEmail(email, byAdmin);
        repository.delete(userEntity);
    }

    @Override
    public SignedInUser getSignedInUser(UserEntity userEntity) {
        userTokenRepository.deleteByUserId(userEntity.getId());
        return createSignedUserWithRefreshToken(userEntity);
    }

    private SignedInUser createSignedUserWithRefreshToken(UserEntity userEntity) {
        return createSignedInUser(userEntity).refreshToken(createRefreshToken(userEntity));
    }

    private SignedInUser createSignedInUser(UserEntity userEntity) {
        String token = tokenManager.create(
                org.springframework.security.core.userdetails.User.builder()
                        .username(userEntity.getUsername())
                        .password(userEntity.getPassword())
                        .authorities(
                                Objects.nonNull(userEntity.getRole()) ? userEntity.getRole().name() : "")
                        .build());
        return new SignedInUser()
                .username(userEntity.getUsername())
                .role(userEntity.getRole().name())
                .accessToken(token)
                .userId(userEntity.getId());
    }

    @Override
    public Optional<SignedInUser> getAccessToken(RefreshToken refreshToken) {
        // You may add an additional validation for time that would remove/invalidate
        // the refresh token
        return userTokenRepository
                .findByRefreshToken(refreshToken.getRefreshToken())
                .map(
                        ut -> Optional.of(
                                createSignedInUser(ut.getUser()).refreshToken(refreshToken.getRefreshToken())))
                .orElseThrow(() -> new RuntimeException("Invalid token."));
    }

    @Override
    public void removeRefreshToken(String refreshToken) {
        userTokenRepository
                .findByRefreshToken(refreshToken)
                .ifPresentOrElse(
                        userTokenRepository::delete,
                        () -> {
                            throw new RuntimeException("Invalid token.");
                        });
    }

    @Override
    public UserEntity findUserByEmail(String email, String byAdmin) {
        if (Strings.isBlank(email)) {
            throw new UsernameNotFoundException("Invalid user.");
        }
        final String uname = email.trim();
        Optional<UserEntity> oUserEntity = repository.findByUsername(uname);
        UserEntity userEntity;
        userEntity = oUserEntity.orElse(null);
        if (userEntity == null || (Objects.isNull(byAdmin) && Objects.nonNull(userEntity) && userEntity.isDeleted())) {
            return null;
        }
        return userEntity;
    }

    @Override
    public UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();
        String newId = userEntity.getId();
        BeanUtils.copyProperties(user, userEntity, "createdAt");
        if (Objects.isNull(user.getId()) || Objects.equals(user.getId(), "")) {
            userEntity.setId(newId);
        }
        userEntity.setRole(RoleEnum.valueOf(user.getRole()));
        if (Objects.nonNull(user.getPassword()) && !Objects.equals(user.getPassword(), "")) {
            userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        return userEntity;
    }

    @Override
    public PasswordValidated verifyPassword(InputPassword inputs) {
        UserEntity userEntity = findUserByEmail(inputs.getEmail(), null);
        if (Objects.isNull(userEntity)) {
            return new PasswordValidated().passwordValidated(false);
        }
        return new PasswordValidated().passwordValidated(
                bCryptPasswordEncoder.matches(inputs.getPassword(), userEntity.getPassword()));
    }

    private String createRefreshToken(UserEntity user) {
        String token = RandomHolder.randomKey(128);
        UserTokenEntity userToken = new UserTokenEntity();
        userToken.setRefreshToken(token);
        userToken.setUser(user);
        userTokenRepository.save(userToken);
        return token;
    }

    // https://stackoverflow.com/a/31214709/109354
    // or can use org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(n)
    private static class RandomHolder {
        static final Random random = new SecureRandom();

        public static String randomKey(int length) {
            return String.format(
                    "%" + length + "s", new BigInteger(length * 5 /* base 32,2^5 */, random).toString(32))
                    .replace('\u0020', '0');
        }
    }
}