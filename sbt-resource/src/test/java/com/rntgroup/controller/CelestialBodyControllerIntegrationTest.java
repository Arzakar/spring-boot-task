package com.rntgroup.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.dto.CelestialBodyDto;
import com.rntgroup.mapper.CelestialBodyMapper;
import com.rntgroup.repository.CelestialBodyRepository;
import com.rntgroup.service.CelestialBodyService;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc(addFilters = false)
class CelestialBodyControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CelestialBodyRepository celestialBodyRepository;

    @Autowired
    private CelestialBodyService celestialBodyService;

    @Autowired
    private CelestialBodyMapper celestialBodyMapper;

    private List<CelestialBodyDto> celestialBodies;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @BeforeEach
    void setUp() {
        celestialBodies = celestialBodyRepository.findAll().stream()
                .map(celestialBodyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Test
    void shouldReturnAllCelestialBodies() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/celestial-bodies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(celestialBodies.size())))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(celestialBodies.stream()
                        .map(CelestialBodyDto::getName)
                        .toArray())))
                .andExpect(jsonPath("$[*].mass", containsInAnyOrder(celestialBodies.stream()
                        .map(CelestialBodyDto::getMass)
                        .toArray())))
                .andExpect(jsonPath("$[*].radius", containsInAnyOrder(celestialBodies.stream()
                        .map(CelestialBodyDto::getRadius)
                        .toArray())));
    }

    @Test
    void shouldReturnCelestialBodyByName() throws Exception {
        CelestialBodyDto testBody = celestialBodies.get(0);

        mvc.perform(MockMvcRequestBuilders
                        .get("/celestial-bodies/{name}", testBody.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testBody.getName()))
                .andExpect(jsonPath("$.mass").value(testBody.getMass()))
                .andExpect(jsonPath("$.radius").value(testBody.getRadius()));
    }

    @Test
    @Transactional
    void shouldCreateCelestialBody() throws Exception {
        CelestialBodyDto savedTestBody = new CelestialBodyDto().setName("Pluto");

        mvc.perform(MockMvcRequestBuilders
                        .post("/celestial-bodies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(savedTestBody.getName()))
                .andExpect(jsonPath("$.mass").value(nullValue()))
                .andExpect(jsonPath("$.radius").value(nullValue()));

        assertTrue(celestialBodyRepository.findByName("Pluto").isPresent());
    }

    @Test
    @Transactional
    void shouldUpdateCelestialBody() throws Exception {
        CelestialBodyDto updatedTestBody = new CelestialBodyDto().setName("Solar").setMass(1d).setRadius(1d);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/celestial-bodies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedTestBody.getName()))
                .andExpect(jsonPath("$.mass").value(updatedTestBody.getMass()))
                .andExpect(jsonPath("$.radius").value(updatedTestBody.getRadius()));

        assertEquals(updatedTestBody, celestialBodyMapper.toDto(celestialBodyRepository.findByName("Solar").get()));
    }

    @Test
    @Transactional
    void shouldDeleteCelestialBodyByName() throws Exception {
        Long deletedBodyId = celestialBodyRepository.findByName("Solar").get().getId();

        mvc.perform(MockMvcRequestBuilders
                        .delete("/celestial-bodies/{name}", "Solar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(deletedBodyId));
    }

    @Test
    void shouldThrowCelestialBodyNotFoundByNameException() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/celestial-bodies/{name}", "InvalidName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("CelestialBody not found by name = InvalidName"))
                .andExpect(jsonPath("$.stackTrace").value(nullValue()));
    }

    @Test
    void shouldThrowCelestialBodyBadRequestException() throws Exception {
        CelestialBodyDto updatedTestBody = new CelestialBodyDto().setName("InvalidName").setMass(1d).setRadius(1d);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/celestial-bodies", "InvalidName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTestBody)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("CelestialBody with name `InvalidName` not found for update"))
                .andExpect(jsonPath("$.stackTrace").value(nullValue()));
    }

}