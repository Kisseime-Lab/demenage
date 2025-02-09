package com.startup.demenage.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.startup.demenage.AppConfig;
import com.startup.demenage.domain.AnnonceDomain;
import com.startup.demenage.dto.AnnonceDto;
import com.startup.demenage.model.Annonce;
import com.startup.demenage.repository.AnnonceRepository;
import com.startup.demenage.service.AnnonceService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AnnonceControllerTest {

        // @MockitoBean
        // private AnnonceService service;

        // @MockitoBean
        // private AnnonceDto annonceDto;

        @Autowired
        private MockMvc mockMvc;

        // @MockitoBean
        // private AnnonceRepository repository;

        private Page<AnnonceDomain> page;
        private Page<Annonce> page2;
        private PageRequest pageable = PageRequest.of(0, 12);

        @BeforeEach
        public void setup() {
                ObjectMapper mapper = new AppConfig().objectMapper();
                JacksonTester.initFields(this, mapper);
                // mockMvc = MockMvcBuilders.standaloneSetup(controller)
                // .setControllerAdvice(new GlobalCatcher())
                // // .alwaysDo(print())
                // .build();

                final Instant now = Instant.now();
        }

        @Test
        @DisplayName("recuperer toutes les annonces recentes")
        public void testGetLatestAnnonce() throws Exception {
                // given*
                // page2 = new PageImpl<Annonce>(List.of(), pageable, 0);
                // page = new PageImpl<AnnonceDomain>(List.of(), pageable, 0);
                // given(annonceDto.toListModel(page)).willReturn(page2);

                // given(service.getLastestAnnonces("", "", "", 0, 12, null))
                // .willReturn(page);

                // given(repository.findByDepartureCityContainingAndDestinationCityContaining("",
                // "", pageable))
                // .willReturn(page);

                // when
                MockHttpServletResponse response = mockMvc.perform(get("/api/v1/annonces")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                // .andExpect(jsonPath("$.content").isArray())
                                .andReturn().getResponse();

                // then
                // assertThat(response.).isEqualTo(page.toString());
                // verify(repository,
                // times(1)).findByDepartureCityContainingAndDestinationCityContaining("", "",
                // pageable);
        }

        @DisplayName("recuperer ")
        public void testGetLatestAnnonceWithFilter() throws Exception {

        }

}
