package com.rntgroup.exception;

public class CelestialBodyException extends RuntimeException {

    protected CelestialBodyException(String message) {
        super(message);
    }

    public static class CelestialBodyNotFoundByNameException extends CelestialBodyException {

        public CelestialBodyNotFoundByNameException(String name) {
            super(String.format("CelestialBody not found by name = %s", name));
        }
    }

    public static class CelestialBodyBadRequestException extends CelestialBodyException {

        public CelestialBodyBadRequestException(String name) {
            super(String.format("CelestialBody with name `%s` not found for update", name));
        }
    }
}
