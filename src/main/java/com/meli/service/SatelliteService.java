package com.meli.service;

import com.meli.data.SatelliteLiveData;
import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.SatelliteDto;
import com.meli.error_handler.BadRequestException;
import com.meli.error_handler.NotFoundException;
import com.meli.helper.Decoder;
import com.meli.helper.SatelliteMapper;
import com.meli.helper.Triangulate;
import com.meli.model.Location;
import com.meli.model.Satellite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SatelliteService {

    SatelliteLiveData satelliteLiveData;

    @Autowired
    public SatelliteService(SatelliteLiveData satelliteLiveData) {
        this.satelliteLiveData = satelliteLiveData;
    }

    /**
     * Calculates the location of the ship based in the distance to the satellites and decodes the recieved meesage
     * @param requestDto - a list of satellites with their specific name, distance to the ship and message recieved
     * @return MessageLocationResponseDto - the location of the ship and the decoded message
     */
    public MessageLocationResponseDto getLocationMessage(MessageLocationRequestDto requestDto) {
        if (requestDto.getSatellites().size() != 3)
            throw new BadRequestException("Satellites must be three");

        if (requestDto.getSatellites().stream().anyMatch(n -> !SatelliteMapper.isKnownLocation(n.getName())))
            throw new BadRequestException("Satellites must be sato, kenobi and skywalker");

        return MessageLocationResponseDto.builder()
                .location(getLocation(SatelliteMapper.mapListToSet(requestDto.getSatellites())))
                .message(decodeMessage(requestDto.getSatellites()
                        .stream()
                        .map(SatelliteDto::getMessage)
                        .collect(Collectors.toList()))).build();
    }

    /**
     * Calculates the location of the ship based in the distance to the previously stored satellites and decodes the recieved meesage
     * @return MessageLocationResponseDto - the location of the ship and the decoded message
     */
    public MessageLocationResponseDto getLocationMessage() {
        if (satelliteLiveData.satellites.size() != 3)
            throw new NotFoundException();

        List<List<String>> messages = new ArrayList<>();

        satelliteLiveData.satellites.forEach(it -> messages.add(it.getMessage()));

        return MessageLocationResponseDto.builder()
                .message(decodeMessage(messages))
                .location(getLocation(satelliteLiveData.satellites))
                .build();
    }

    /**
     * Adds a satellite to the list
     * @param satelliteDto - the information of the satellite
     */
    public void addSatellite(SatelliteDto satelliteDto) {
        if (!SatelliteMapper.isKnownLocation(satelliteDto.getName()))
            throw new BadRequestException("Satellite " + satelliteDto.getName() + " is unnknown");

        satelliteLiveData.satellites.removeIf(s -> s.getName().equals(satelliteDto.getName()));
        satelliteLiveData.satellites.add(SatelliteMapper.map(satelliteDto));
    }

    /**
     * Calculates the location of the ship based on a set of satellites
     * @param satellites - a set of satellites
     * @return - Location - the ship's location
     */
    private Location getLocation(Set<Satellite> satellites) {
        Location location = Triangulate.getIntersection(satellites);

        if (location == null)
            throw new NotFoundException();

        return location;
    }

    /**
     * Decodes the message based on three recieved messages
     * @param messages - List of messages recieved from each satellite
     * @return String - The decoded message
     */
    private String decodeMessage(List<List<String>> messages) {
        String message = Decoder.decodeMessage(messages);

        if (message == null)
            throw new NotFoundException();

        return message;
    }
}
