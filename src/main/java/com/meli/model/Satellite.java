package com.meli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class Satellite {

    private String name;
    private double distance;
    private Location location;
    private List<String> message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Satellite satellite = (Satellite) o;
        return Objects.equals(name, satellite.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
