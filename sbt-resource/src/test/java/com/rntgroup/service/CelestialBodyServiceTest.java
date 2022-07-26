package com.rntgroup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.rntgroup.dto.CelestialBodyDto;
import com.rntgroup.exception.CelestialBodyException;
import com.rntgroup.mapper.CelestialBodyMapper;
import com.rntgroup.model.CelestialBody;
import com.rntgroup.repository.CelestialBodyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CelestialBodyServiceTest {

    @Mock
    private CelestialBodyRepository celestialBodyRepository;

    @Spy
    private CelestialBodyMapper celestialBodyMapper = Mappers.getMapper(CelestialBodyMapper.class);

    @InjectMocks
    private CelestialBodyService celestialBodyService;

    @Test
    void shouldReturnCelestialBodyDtoByName() {
        CelestialBodyDto testBody = new CelestialBodyDto().setName("TestBody").setMass(10d).setRadius(5d);

        when(celestialBodyRepository.findByName(any(String.class))).thenReturn(Optional.of(
                new CelestialBody().setId(0L).setName(testBody.getName()).setMass(testBody.getMass()).setRadius(testBody.getRadius())
        ));

        assertEquals(testBody, celestialBodyService.getByName("TestBody"));
    }

    @Test
    void shouldThrowNotFoundByNameException() {
        when(celestialBodyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        var thrown = assertThrows(CelestialBodyException.CelestialBodyNotFoundByNameException.class, () ->
                celestialBodyService.getByName("TestBody"));

        assertEquals(new CelestialBodyException.CelestialBodyNotFoundByNameException("TestBody").getMessage(), thrown.getMessage());
    }

    @Test
    void shouldThrowBadRequestException() {
        when(celestialBodyRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        var thrown = assertThrows(CelestialBodyException.CelestialBodyBadRequestException.class, () ->
                celestialBodyService.update(new CelestialBodyDto().setName("TestBody")));

        assertEquals(new CelestialBodyException.CelestialBodyBadRequestException("TestBody").getMessage(), thrown.getMessage());
    }
}