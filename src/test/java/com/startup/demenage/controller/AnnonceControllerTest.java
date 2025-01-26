package com.startup.demenage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import com.startup.demenage.controllers.AnnonceController;
import com.startup.demenage.dto.AnnonceDto;
import com.startup.demenage.entity.AnnonceEntity;
import com.startup.demenage.exception.GlobalCatcher;
import com.startup.demenage.service.AnnonceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AnnonceControllerTest {

    @Mock
    private AnnonceService service;
    @Mock
    private AnnonceDto annonceDto;

    private MockMvc mockMvc;
    @InjectMocks
    private AnnonceController controller;

    private Page<AnnonceEntity> page;

    @BeforeEach
    public void setup() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
        MappingJackson2HttpMessageConverter();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new GlobalCatcher())
        .setMessageConverters(mappingJackson2HttpMessageConverter)
        .alwaysDo(print())
        .build();

        PageRequest pageable = PageRequest.of(0, 12);

        page = new PageImpl<AnnonceEntity>(List.of(), pageable, 0);
    }

    @Test
    @DisplayName("get annonce")
    public void testGetLatestAnnonce() throws Exception {
        given(service.getLastestAnnonces("", "", "",
         0, 10, null))
         .willReturn(page);

         MockHttpServletResponse response = mockMvc.perform(get("/api/v1/annonces")
         .contentType(MediaType.APPLICATION_JSON)
         .accept(MediaType.APPLICATION_JSON))
         .andDo(print())
        .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
}
