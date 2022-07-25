package com.rntgroup.repository;

import com.rntgroup.model.CelestialBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CelestialBodyRepository extends JpaRepository<CelestialBody, Long> {

    Optional<CelestialBody> findByName(String name);

    @Transactional
    CelestialBody deleteByName(String name);
}
