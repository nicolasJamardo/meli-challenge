package com.meli.helper;

import com.meli.dto.SatelliteDto;
import com.meli.model.Location;
import com.meli.model.Satellite;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SatelliteMapper {

    private static Map<String, Location> knownLocations = new HashMap<String, Location>() {{
        put("kenobi", new Location(-500, -200));
        put("skywalker", new Location(100, -100));
        put("sato", new Location(500, 100));
    }};

    /**
     * Maps a satelliteDto into a Satellite
     * @param satelliteDto
     * @return Satellite
     */
    public static Satellite map(SatelliteDto satelliteDto) {
        return new Satellite(satelliteDto.getName(),
                satelliteDto.getDistance(),
                knownLocations.get(satelliteDto.getName().toLowerCase()),
                satelliteDto.getMessage());
    }

    /**
     * Maps a list of satellitesDtos to a set of Satellites
     * @param satelliteDtos
     * @return Set of satellites
     */
    public static Set<Satellite> mapListToSet(List<SatelliteDto> satelliteDtos) {
        return satelliteDtos.stream()
                .map(SatelliteMapper::map)
                .collect(Collectors.toSet());
    }

    /**
     * Valides if the name of the satellite recieved is valid
     * @param name
     * @return boolean
     */
    public static boolean isKnownLocation(String name) {
        return knownLocations.keySet().stream().anyMatch(n -> n.equals(name));
    }
}
