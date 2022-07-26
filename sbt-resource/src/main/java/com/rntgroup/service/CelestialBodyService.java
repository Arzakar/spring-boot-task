package com.rntgroup.service;

import com.rntgroup.dto.CelestialBodyDto;
import com.rntgroup.exception.CelestialBodyException.CelestialBodyBadRequestException;
import com.rntgroup.exception.CelestialBodyException.CelestialBodyNotFoundByNameException;
import com.rntgroup.mapper.CelestialBodyMapper;
import com.rntgroup.model.CelestialBody;
import com.rntgroup.repository.CelestialBodyRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CelestialBodyService {

    CelestialBodyRepository celestialBodyRepository;
    CelestialBodyMapper celestialBodyMapper;

    public CelestialBodyDto create(CelestialBodyDto celestialBodyDto) {
        CelestialBody celestialBody = celestialBodyRepository.save(celestialBodyMapper.toModel(celestialBodyDto));
        return celestialBodyMapper.toDto(celestialBody);
    }

    public List<CelestialBodyDto> getAll() {
        return celestialBodyRepository.findAll().stream()
                .map(celestialBodyMapper::toDto)
                .collect(Collectors.toList());
    }

    public CelestialBodyDto getByName(String name) {
        CelestialBody celestialBody = celestialBodyRepository.findByName(name)
                .orElseThrow(() -> new CelestialBodyNotFoundByNameException(name));
        return celestialBodyMapper.toDto(celestialBody);
    }

    public CelestialBodyDto update(CelestialBodyDto celestialBodyDto) {
        CelestialBody updatableCelestialBody = celestialBodyRepository.findByName(celestialBodyDto.getName())
                .orElseThrow(() -> new CelestialBodyBadRequestException(celestialBodyDto.getName()));

        updatableCelestialBody.setMass(celestialBodyDto.getMass())
                .setRadius(celestialBodyDto.getRadius());

        return celestialBodyMapper.toDto(celestialBodyRepository.save(updatableCelestialBody));
    }

    public Long deleteByName(String name) {
        return celestialBodyRepository.deleteByName(name);
    }

}
