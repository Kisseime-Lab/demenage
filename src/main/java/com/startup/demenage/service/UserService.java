package com.startup.demenage.service;

import java.util.Optional;

import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.model.InputPassword;
import com.startup.demenage.model.PasswordValidated;
import com.startup.demenage.model.RefreshToken;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;

public interface UserService {
    UserDomain findUserByEmail(String email, String byAdmin);

    Optional<SignedInUser> createUser(User user);

    SignedInUser getSignedInUser(UserDomain userEntity);

    Optional<SignedInUser> getAccessToken(RefreshToken refToken);

    void removeRefreshToken(String refreshToken);

    UserDomain toEntity(User user);

    UserDomain updateUser(User user, String email);

    void deleteUser(String email, String byAdmin);

    UserDomain getUserById(String id, String byAdmin);

    PasswordValidated verifyPassword(InputPassword inputs);
}
