package com.meli.helper;

import com.meli.model.Location;
import com.meli.model.Satellite;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.StrictMath.round;

@Component
public class Triangulate {

    /**
     * Triangulates the distance of the ship based on the location of the satellites and their location to the ship
     *
     * @param satelliteSet
     * @return Location
     */
    public static Location getIntersection(Set<Satellite> satelliteSet) {
        List<Satellite> satellites = new ArrayList<>(satelliteSet);
        Location firstSatLocation = satellites.get(0).getLocation();
        Location secondSatLocation = satellites.get(1).getLocation();
        double firstSatDistance = satellites.get(0).getDistance();
        double secondSatDistance = satellites.get(1).getDistance();

        double distance = firstSatLocation.getDistance(secondSatLocation);
        if (distance > firstSatDistance + secondSatDistance || distance < Math.abs(firstSatDistance - secondSatDistance) || (distance == 0 && firstSatDistance == secondSatDistance)) {
            return null;
        }

        double a = (Math.pow(firstSatDistance, 2) - Math.pow(secondSatDistance, 2) + Math.pow(distance, 2)) / (2 * distance);
        double height = Math.sqrt(Math.pow(firstSatDistance, 2) - Math.pow(a, 2));
        double x1 = firstSatLocation.getX() + a * (secondSatLocation.getX() - firstSatLocation.getX()) / distance;
        double y1 = firstSatLocation.getY() + a * (secondSatLocation.getY() - firstSatLocation.getY()) / distance;
        Location thirdSatelliteLocation = satellites.get(2).getLocation();
        double thirdSatelliteDistance = satellites.get(2).getDistance();

        List<Location> locations = new ArrayList<>();
        locations.add(new Location(x1 + height * (secondSatLocation.getY() - firstSatLocation.getY()) / distance, y1 - height * (secondSatLocation.getX() - firstSatLocation.getX()) / distance));
        locations.add(new Location(x1 - height * (secondSatLocation.getY() - firstSatLocation.getY()) / distance, y1 + height * (secondSatLocation.getX() - firstSatLocation.getX()) / distance));

        Location location = null;
        if (round((Math.pow(thirdSatelliteDistance, 2))) == round(Math.pow((thirdSatelliteLocation.getX() - locations.get(1).getX()), 2) + Math.pow((thirdSatelliteLocation.getY() - locations.get(1).getY()), 2))) {
            location = locations.get(1);
        } else if (round((Math.pow(thirdSatelliteDistance, 2))) == round(Math.pow((thirdSatelliteLocation.getX() - locations.get(0).getX()), 2) + Math.pow((thirdSatelliteLocation.getY() - locations.get(0).getY()), 2))) {
            location = locations.get(0);
        }
        return location == null ? null : location.round();
    }
}
