package com.startup.demenage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.startup.demenage.model.SignInReq;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;
import com.startup.demenage.repository.UserTokenRepository;
import com.startup.demenage.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SigninControllerTests {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    UserService service;
    @Autowired
    UserTokenRepository userTokenRepository;

    private User user = TestUtils.createUser();

    @BeforeAll
    void setup() {
        restTemplate.postForEntity("/api/v1/users",
                user, SignedInUser.class).getBody().getUserId();
    }

    @AfterAll
    void tearDown() {
        service.deleteUser(user.getUsername(), "true");
    }

    @Test
    void connexion_reussie() {
        SignInReq signInReq = TestUtils.createSignInReq();
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/auth/token",
                signInReq, SignedInUser.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TestUtils.username, response.getBody().getUsername());
        assertNotNull(response.getBody().getAccessToken());
        assertNotNull(response.getBody().getRefreshToken());
    }

    @Test
    void mauvais_password() {
        SignInReq signInReq = TestUtils.createSignInReq();
        signInReq.setPassword("NoTest1234#");
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/auth/token",
                signInReq, SignedInUser.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void mauvais_format() {
        SignInReq signInReq = TestUtils.createSignInReq();
        signInReq.setPassword("NoTest1234");
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/auth/token",
                signInReq, SignedInUser.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
