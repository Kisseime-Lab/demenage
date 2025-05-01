package com.startup.demenage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.model.User;
import com.startup.demenage.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SignupControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserService service;

    private User user = TestUtils.createUser();;

    @AfterAll
    void tearDown() {
        service.deleteUser(user.getUsername(), "true");
    }

    @Test
    void creation_compte_reussie() {
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/users",
                user, SignedInUser.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ktevot@gmail.com", response.getBody().getUsername());
        assertNotNull(response.getBody().getAccessToken());
        assertNotNull(response.getBody().getRefreshToken());
    }

    @Test
    void champ_obligatoire_manquant() {
        user = TestUtils.createUser();
        user.setPhone(null);
        ResponseEntity<SignedInUser> response = restTemplate.postForEntity("/api/v1/users",
                user, SignedInUser.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }
}
