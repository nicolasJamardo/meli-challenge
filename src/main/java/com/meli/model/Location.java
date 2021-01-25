package com.meli.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Location {

    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getDistance(Location l) {
        return Math.sqrt(Math.pow(l.x - this.x, 2) + Math.pow(l.y - this.y, 2));
    }

    public Location round() {
        return new Location(Math.round(this.getX()), Math.round(this.getY()));
    }
}

