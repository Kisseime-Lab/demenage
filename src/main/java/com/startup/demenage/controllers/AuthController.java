package com.startup.demenage.controllers;

import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.startup.demenage.UserApi;
import com.startup.demenage.dto.UserDto;
import com.startup.demenage.entity.UserEntity;
import com.startup.demenage.model.InputPassword;
import com.startup.demenage.model.PasswordValidated;
import com.startup.demenage.model.RefreshToken;
import com.startup.demenage.model.SignInReq;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;
import com.startup.demenage.service.UserService;
import com.startup.demenage.service.serviceImpl.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
public class AuthController implements UserApi {

    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    private final UserDto userDto;

    public AuthController(UserService service, PasswordEncoder passwordEncoder, UserDto userDto) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.userDto = userDto;
    }

    @Override
    public ResponseEntity<SignedInUser> getAccessToken(@Valid RefreshToken refreshToken) {
        return ok(service.getAccessToken(refreshToken).orElseThrow(RuntimeException::new));
    }


    @Override
    public ResponseEntity<SignedInUser> signIn(@Valid SignInReq signInReq) {
        UserEntity userEntity = service.findUserByEmail(signInReq.getUsername(), null);
        if (passwordEncoder.matches(signInReq.getPassword(), userEntity.getPassword())) {
            return ok(service.getSignedInUser(userEntity));
        }
        throw new InsufficientAuthenticationException("Unauthorized.");
    }

    @Override
    public ResponseEntity<Void> signOut(@Valid RefreshToken refreshToken) throws Exception {
        service.removeRefreshToken(refreshToken.getRefreshToken());
        return accepted().build();
    }

    @Override
    public ResponseEntity<User> getUserbyEmailOrId(String email, String user_id, String byAdmin) throws Exception {
        if (Objects.nonNull(user_id) && !user_id.isEmpty()) {
            return status(HttpStatus.OK).body(userDto.toModel(service.getUserById(user_id, byAdmin)));
        }
        return status(HttpStatus.OK).body(userDto.toModel(service.findUserByEmail(email, byAdmin)));
    }

    @Override
    public ResponseEntity<Void> deleteUser(String email, String byAdmin) throws Exception {
        service.deleteUser(email, byAdmin);
        return status(HttpStatus.OK).build();
    }

    

    @Override
    public ResponseEntity<PasswordValidated> verifyPassword(@Valid InputPassword inputPassword) throws Exception {
        // TODO Auto-generated method stub
        return status(HttpStatus.OK).body(service.verifyPassword(inputPassword));
    }

    @Override
    public ResponseEntity<User> updateUser(@Valid String email, @Valid String byAdmin, @Valid User user)
            throws Exception {
            return status(HttpStatus.OK).body(userDto.toModel(service.updateUser(user, email)));
    }

    @Override
    public ResponseEntity<SignedInUser> signUp(@Valid User user) {
        // Have a validation for all required fields.
        return status(HttpStatus.CREATED).body(service.createUser(user).get());
    }
}