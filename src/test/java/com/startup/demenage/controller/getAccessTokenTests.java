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

import com.startup.demenage.model.RefreshToken;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;
import com.startup.demenage.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetAccessTokenTests {
    @Autowired
    TestRestTemplate restTemplate;
    private static SignedInUser signedInUser;

    @Autowired
    private UserService service;

    private User user = TestUtils.createUser();

    @BeforeAll
    void setup() {
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/users",
                user, SignedInUser.class);
        signedInUser = response.getBody();
    }

    @AfterAll
    void tearDown() {
        service.deleteUser(user.getUsername(), "true");
    }

    @Test
    void recuperation_reussie() {
        RefreshToken refreshToken = new RefreshToken().refreshToken(signedInUser.getRefreshToken());
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/auth/token/refresh",
                refreshToken, SignedInUser.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(refreshToken.getRefreshToken(), response.getBody().getRefreshToken());
    }

}
