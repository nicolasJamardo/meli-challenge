package com.meli.helper;

import com.meli.dto.SateliteDto;
import com.meli.model.Location;
import com.meli.model.Satelite;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SateliteMapper {

    private static Map<String, Location> knownLocations = new HashMap<String, Location>() {{
        put("kenobi", new Location(-500, -200));
        put("skywalker", new Location(100, -100));
        put("sato", new Location(500, 100));
    }};


    /**
     * @param sateliteDto
     * @return
     */
    public static Satelite map(SateliteDto sateliteDto) {
        return new Satelite(sateliteDto.getName(),
                sateliteDto.getDistance(),
                knownLocations.get(sateliteDto.getName().toLowerCase()),
                sateliteDto.getMessage());
    }

    /**
     * @param sateliteDtos
     * @return
     */
    public static Set<Satelite> mapListToSet(List<SateliteDto> sateliteDtos) {
        return sateliteDtos.stream()
                .map(SateliteMapper::map)
                .collect(Collectors.toSet());
    }

    public static boolean isKnownLocation(String name) {
        return knownLocations.keySet().stream().anyMatch(n-> n.equals(name));
    }
}
