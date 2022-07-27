package com.rntgroup.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.dto.CelestialBodyDto;
import com.rntgroup.service.CelestialBodyService;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(CelestialBodyController.class)
@ActiveProfiles("local")
class CelestialBodyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CelestialBodyService celestialBodyService;

    private List<CelestialBodyDto> celestialBodies;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @BeforeEach
    void setUp() {
        celestialBodies = List.of(
                new CelestialBodyDto().setName("TestSolar").setMass(1000d).setRadius(200d),
                new CelestialBodyDto().setName("TestEarth").setMass(50d).setRadius(5d)
        );
    }

    @Test
    void shouldReturnAllCelestialBodies() throws Exception {
        when(celestialBodyService.getAll()).thenReturn(celestialBodies);

        mvc.perform(MockMvcRequestBuilders
                        .get("/celestial-bodies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
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

        when(celestialBodyService.getByName(testBody.getName())).thenReturn(testBody);

        mvc.perform(MockMvcRequestBuilders
                        .get("/celestial-bodies/{name}", testBody.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testBody.getName()))
                .andExpect(jsonPath("$.mass").value(testBody.getMass()))
                .andExpect(jsonPath("$.radius").value(testBody.getRadius()));
    }

    @Test
    void shouldCreateCelestialBody() throws Exception {
        CelestialBodyDto savedTestBody = new CelestialBodyDto().setName("TestBody").setMass(1d).setRadius(1d);

        when(celestialBodyService.create(savedTestBody)).thenReturn(savedTestBody);

        mvc.perform(MockMvcRequestBuilders
                        .post("/celestial-bodies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(savedTestBody.getName()))
                .andExpect(jsonPath("$.mass").value(savedTestBody.getMass()))
                .andExpect(jsonPath("$.radius").value(savedTestBody.getRadius()));
    }

    @Test
    void shouldUpdateCelestialBody() throws Exception {
        CelestialBodyDto updatedTestBody = new CelestialBodyDto().setName("TestSolar").setMass(1d).setRadius(1d);

        when(celestialBodyService.update(updatedTestBody)).thenReturn(updatedTestBody);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/celestial-bodies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedTestBody.getName()))
                .andExpect(jsonPath("$.mass").value(updatedTestBody.getMass()))
                .andExpect(jsonPath("$.radius").value(updatedTestBody.getRadius()));
    }

    @Test
    void shouldDeleteCelestialBodyByName() throws Exception {
        CelestialBodyDto deletedTestBody = celestialBodies.get(0);

        when(celestialBodyService.deleteByName(deletedTestBody.getName())).thenReturn(0L);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/celestial-bodies/{name}", deletedTestBody.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0L));
    }
}