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

import com.startup.demenage.model.RefreshToken;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class getAccessTokenTests {

    private static SignedInUser signedInUser;

    @BeforeAll
    public static void setup(@Autowired TestRestTemplate restTemplate) {
        User user = TestUtils.createUser();
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/users",
                user, SignedInUser.class);
        signedInUser = response.getBody();
    }

    @Test
    public void recuperation_reussie(@Autowired TestRestTemplate restTemplate) {
        RefreshToken refreshToken = new RefreshToken().refreshToken(signedInUser.getRefreshToken());
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/auth/token/refresh",
                refreshToken, SignedInUser.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(refreshToken.getRefreshToken(), response.getBody().getRefreshToken());
    }

}
