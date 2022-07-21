package com.rntgroup.mapper;

import com.rntgroup.dto.CelestialBodyDto;
import com.rntgroup.model.CelestialBody;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CelestialBodyMapper {

    CelestialBodyDto toDto(CelestialBody celestialBody);

    CelestialBody toModel(CelestialBodyDto celestialBodyDto);
}
