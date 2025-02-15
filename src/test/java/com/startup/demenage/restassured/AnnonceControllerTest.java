package com.startup.demenage.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.startup.demenage.domain.AdresseDomain;
import com.startup.demenage.domain.AnnonceDomain;
import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.repository.UserRepository;
import com.startup.demenage.security.JwtManager;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AnnonceControllerTest {

    @LocalServerPort
    private int port;

    private UserDomain user;
    @Autowired
    private UserRepository repository;
    @Autowired
    private JwtManager tokenManager;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void get_latest_annonces() {
        given()
                .when()
                .get("/api/v1/annonces")
                .then()
                .statusCode(200)
                .body("content", hasSize(0));
    }

    @Test
    void creer_une_annonce() throws JsonProcessingException {
        user = getUser();

        AnnonceDomain annonce = createAnnonce(user);
        String token = getToken(user);
        repository.save(user);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(mapper.writeValueAsString(annonce))
                .when()
                .post("/api/v1/annonces")
                .then()
                .statusCode(201)
                .body("titre", equalTo("Déménagement Paris-Lyon"));

    }

    @Test
    @DisplayName("consulter une annonce")
    void consulter_une_annonce() {

    }

    private AnnonceDomain createAnnonce(UserDomain userDomain) {
        AnnonceDomain annonce = new AnnonceDomain();

        // Remplissage des champs
        annonce.setId(null); // L'ID peut être généré automatiquement
        annonce.setTitre("Déménagement Paris-Lyon");

        // Création de l'objet Departure et Destination (type AdresseDomain)
        AdresseDomain departure = new AdresseDomain();
        departure.setCity("Paris");
        departure.setLibelle("10 Rue de Paris");
        departure.setZipCode("75001");

        AdresseDomain destination = new AdresseDomain();
        destination.setCity("Lyon");
        destination.setLibelle("20 Rue de Lyon");
        destination.setZipCode("69001");

        annonce.setDeparture(departure);
        annonce.setDestination(destination);

        // Photos (ici supposées comme String, mais elles peuvent être une liste ou un
        // autre type)
        annonce.setPhotos("photo1.jpg, photo2.jpg");

        // Distance et prix
        annonce.setDistance(465);
        annonce.setPrix(300);
        annonce.setAuthor(userDomain);
        return annonce;
    }

    private String getToken(UserDomain user) {
        return tokenManager.create(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(
                                Objects.nonNull(user.getRole()) ? user.getRole().name() : "")
                        .build());
    }

    private UserDomain getUser() {
        UserDomain user = new UserDomain();
        user.setFirstname("Kisseime");
        user.setLastname("TEVOT");
        user.setUsername("kisseime@gmail.com");
        user.setPassword("Password12@");
        return user;
    }
}
