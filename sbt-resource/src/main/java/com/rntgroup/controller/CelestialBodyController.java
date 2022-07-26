package com.rntgroup.controller;

import com.rntgroup.dto.CelestialBodyDto;
import com.rntgroup.service.CelestialBodyService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/celestial-bodies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CelestialBodyController {

    CelestialBodyService celestialBodyService;

    @GetMapping
    public List<CelestialBodyDto> getAll() {
        return celestialBodyService.getAll();
    }

    @PostMapping
    public CelestialBodyDto create(@RequestBody CelestialBodyDto celestialBodyDto) {
        return celestialBodyService.create(celestialBodyDto);
    }

    @GetMapping(path = "/{name}")
    public CelestialBodyDto getByName(@PathVariable("name") String name) {
        return celestialBodyService.getByName(name);
    }

    @PatchMapping
    public CelestialBodyDto update(@RequestBody CelestialBodyDto celestialBodyDto) {
        return celestialBodyService.update(celestialBodyDto);
    }


    @DeleteMapping(path = "/{name}")
    public Long deleteByName(@PathVariable("name") String name) {
        return celestialBodyService.deleteByName(name);
    }
}
