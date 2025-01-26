package com.startup.demenage.controller;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.startup.demenage.controllers.AuthController;
import com.startup.demenage.dto.UserDto;
import com.startup.demenage.entity.UserEntity;
import com.startup.demenage.exception.GlobalCatcher;
import com.startup.demenage.model.SignInReq;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.service.UserService;
import com.startup.demenage.service.serviceImpl.UserServiceImpl;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;

import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AuthControlleTest {

    MockMvc mockMvc;

    @Mock
    private UserServiceImpl service;
    @Mock
    private UserDto userDto;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private  AuthController controller;
    private UserEntity userEntity = new UserEntity();
    private SignInReq signInReq = new SignInReq();
    private SignedInUser signedInUser = new SignedInUser();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalCatcher())
                .alwaysDo(print())
                .build();
        
            final Instant now = Instant.now();

        signInReq.setPassword("toto");
        signInReq.setUsername("toto@xyz.fr");

        userEntity.setId("1234");
        userEntity.setUsername("toto@xyz.fr");
        userEntity.setPassword("toto");

        signedInUser.setUserId("1234");
        signedInUser.setUsername("toto@xyz.fr");
        signedInUser.role("CUSTOMER");
    }

    @Test
    @DisplayName("signin with good credentials")
    public void signinWithExistingUser() throws Exception {
        given(service.findUserByEmail("toto@xyz.fr", null))
                .willReturn(userEntity);

        given(passwordEncoder.matches(isA(CharSequence.class), isA(String.class)))
                .willReturn(true);

        given(service.getSignedInUser(userEntity))
                .willReturn(signedInUser);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(signInReq);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/v1/auth/token")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
                System.out.println(" ------------------- " + result.toString());
        // then
        assertThat(controller).isNotNull();
        System.out.println("------------- " + result.toString());
        result.andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(userEntity.getId())))
                .andExpect(jsonPath("username", is(userEntity.getUsername())));
    }

}
