package com.startup.demenage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.startup.demenage.model.SignInReq;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SigninControllerTests {

    @BeforeAll
    public static void setup(@Autowired TestRestTemplate restTemplate) {
        User user = TestUtils.createUser();
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/users",
                user, SignedInUser.class);
        // assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void connexion_reussie(@Autowired TestRestTemplate restTemplate) {
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
    public void mauvais_password(@Autowired TestRestTemplate restTemplate) {
        SignInReq signInReq = TestUtils.createSignInReq();
        signInReq.setPassword("NoTest1234#");
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/auth/token",
                signInReq, SignedInUser.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void mauvais_format(@Autowired TestRestTemplate restTemplate) {
        SignInReq signInReq = TestUtils.createSignInReq();
        signInReq.setPassword("NoTest1234");
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/auth/token",
                signInReq, SignedInUser.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
