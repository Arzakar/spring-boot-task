package com.rntgroup.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rntgroup.IntegrationTest;
import com.rntgroup.model.CelestialBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@IntegrationTest
@DataJpaTest
class CelestialBodyRepositoryIntegrationTest {

    @Autowired
    private CelestialBodyRepository celestialBodyRepository;

    @Test
    void shouldReturnCelestialBodyByName() {
        Optional<CelestialBody> celestialBody = celestialBodyRepository.findByName("Solar");

        assertTrue(celestialBody.isPresent());
        assertAll(
                () -> assertEquals("Solar", celestialBody.get().getName()),
                () -> assertEquals(1989100000000000000000000000000d, celestialBody.get().getMass()),
                () -> assertEquals(695508000d, celestialBody.get().getRadius())
        );
    }

    @Test
    void shouldReturnEmptyByName() {
        Optional<CelestialBody> celestialBody = celestialBodyRepository.findByName("Imagine");

        assertTrue(celestialBody.isEmpty());
    }

    @Test
    @Transactional
    void shouldDeleteCelestialBodyByName() {
        assertEquals(1, celestialBodyRepository.deleteByName("Earth"));
        assertTrue(celestialBodyRepository.findByName("Earth").isEmpty());
    }

    @Test
    @Transactional
    void shouldNotDeleteCelestialBodyByName() {
        assertEquals(0, celestialBodyRepository.deleteByName("Imagine"));
        assertTrue(celestialBodyRepository.findByName("Earth").isPresent());
    }
}