package com.startup.demenage.service;


import java.util.Optional;

import com.startup.demenage.entity.UserEntity;
import com.startup.demenage.model.InputPassword;
import com.startup.demenage.model.PasswordValidated;
import com.startup.demenage.model.RefreshToken;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;

public interface UserService {
    UserEntity findUserByEmail(String email, String byAdmin);
    Optional<SignedInUser> createUser(User user);
    SignedInUser getSignedInUser(UserEntity userEntity);
    Optional<SignedInUser> getAccessToken(RefreshToken refToken);
    void removeRefreshToken(String refreshToken);
    UserEntity toEntity(User user);
    UserEntity updateUser(User user);
    void deleteUser(String email, String byAdmin);
    UserEntity getUserById(String id, String byAdmin);
    PasswordValidated verifyPassword(InputPassword inputs);
}
