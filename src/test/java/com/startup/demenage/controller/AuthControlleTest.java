package com.startup.demenage.controller;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.startup.demenage.controllers.AuthController;
import com.startup.demenage.dto.UserDto;
import com.startup.demenage.entity.UserEntity;
import com.startup.demenage.exception.GlobalCatcher;
import com.startup.demenage.model.SignInReq;
import com.startup.demenage.model.SignedInUser;
import com.startup.demenage.service.UserService;
import com.startup.demenage.service.serviceImpl.UserServiceImpl;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class AuthControlleTest {

    static MockMvc mockMvc;

    private static UserServiceImpl service = mock(UserServiceImpl.class);
    private static UserDto userDto = mock(UserDto.class);
    private static PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private static AuthController controller;
    private static UserEntity userEntity = new UserEntity();
    private static SignInReq signInReq = new SignInReq();
    private static SignedInUser signedInUser = new SignedInUser();

    // @BeforeAll
    // public static void setup() {
    //     controller = new AuthController(service, passwordEncoder, userDto);
    //     mockMvc = MockMvcBuilders.standaloneSetup(controller)
    //     .setControllerAdvice(new GlobalCatcher())
    //     .build();

    //     signInReq.setPassword("toto");
    //     signInReq.setUsername("toto@xyz.fr");

    //     userEntity.setId("toto");
    //     userEntity.setUsername("toto");

    //     signedInUser.setUserId("1234");
    //     signedInUser.setUsername("toto@xyz.fr");
    //     signedInUser.role("CUSTOMER");
    // }

    // @Test
    // @DisplayName("signin with good credentials")
    // public void signinWithNewUser() throws Exception {
    //     given(service.findUserByEmail("toto", null))
    //     .willReturn(userEntity);

    //     given(passwordEncoder.matches(isA(String.class), isA(String.class)))
    //     .willReturn(true);

    //     given(service.getSignedInUser(userEntity))
    //     .willReturn(signedInUser);

    //     //when
    //     ResultActions result = mockMvc.perform(
    //         get("/api/v1/auth/token")
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .accept(MediaType.APPLICATION_JSON)
    //         );

    //     //then
    //     result.andExpect(status().isOk())
    //     .andExpect(jsonPath("userId", is(userEntity.getId())))
    //     .andExpect(jsonPath("username", is(userEntity.getUsername())));
    // }
    
}
