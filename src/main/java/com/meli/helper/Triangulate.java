package com.meli.helper;

import com.meli.model.Location;
import com.meli.model.Satelite;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.StrictMath.round;

@Component
public class Triangulate {

    public static Location getIntersection(Set<Satelite> sateliteSet) {
        List<Satelite> satelites = new ArrayList<>(sateliteSet);
        Location firstSatLocation = satelites.get(0).getLocation();
        Location secondSatLocation = satelites.get(1).getLocation();
        double firstSatDistance = satelites.get(0).getDistance();
        double secondSatDistance = satelites.get(1).getDistance();

        double distance = firstSatLocation.getDistance(secondSatLocation);
        if (distance > firstSatDistance + secondSatDistance || distance < Math.abs(firstSatDistance - secondSatDistance) || (distance == 0 && firstSatDistance == secondSatDistance)) {
            return null;
        }

        double a = (Math.pow(firstSatDistance, 2) - Math.pow(secondSatDistance, 2) + Math.pow(distance, 2)) / (2 * distance);
        double height = Math.sqrt(Math.pow(firstSatDistance, 2) - Math.pow(a, 2));
        double x1 = firstSatLocation.getX() + a * (secondSatLocation.getX() - firstSatLocation.getX()) / distance;
        double y1 = firstSatLocation.getY() + a * (secondSatLocation.getY() - firstSatLocation.getY()) / distance;
        Location thirdSateliteLocation = satelites.get(2).getLocation();
        double thirdSateliteDistance = satelites.get(2).getDistance();

        List<Location> locations = new ArrayList<>();
        locations.add(new Location(x1 + height * (secondSatLocation.getY() - firstSatLocation.getY()) / distance, y1 - height * (secondSatLocation.getX() - firstSatLocation.getX()) / distance));
        locations.add(new Location(x1 - height * (secondSatLocation.getY() - firstSatLocation.getY()) / distance, y1 + height * (secondSatLocation.getX() - firstSatLocation.getX()) / distance));

        Location location = null;
        if (round((Math.pow(thirdSateliteDistance, 2))) == round(Math.pow((thirdSateliteLocation.getX() - locations.get(1).getX()), 2) + Math.pow((thirdSateliteLocation.getY() - locations.get(1).getY()), 2))) {
            location = locations.get(1);
        } else if (round((Math.pow(thirdSateliteDistance, 2))) == round(Math.pow((thirdSateliteLocation.getX() - locations.get(0).getX()), 2) + Math.pow((thirdSateliteLocation.getY() - locations.get(0).getY()), 2))) {
            location = locations.get(0);
        }
        return location == null ? null :location.round();
    }
}
