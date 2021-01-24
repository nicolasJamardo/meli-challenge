package com.meli.service;

import com.meli.data.SateliteLiveData;
import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.SateliteDto;
import com.meli.error_handler.BadRequestException;
import com.meli.error_handler.NotFoundException;
import com.meli.helper.Decoder;
import com.meli.helper.SateliteMapper;
import com.meli.helper.Triangulate;
import com.meli.model.Location;
import com.meli.model.Satelite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SateliteService {

    SateliteLiveData sateliteLiveData;

    @Autowired
    public SateliteService(SateliteLiveData sateliteLiveData) {
        this.sateliteLiveData = sateliteLiveData;
    }

    /**
     * @param requestDto
     * @return
     */
    public MessageLocationResponseDto getLocationMessage(MessageLocationRequestDto requestDto) {
        if (requestDto.getSatellites().size() != 3)
            throw new BadRequestException("Satelites must be three");

        if (requestDto.getSatellites().stream().anyMatch(n -> !SateliteMapper.isKnownLocation(n.getName())))
            throw new BadRequestException("Satelites must be sato, kenobi and skywalker");

        return MessageLocationResponseDto.builder()
                .location(getLocation(SateliteMapper.mapListToSet(requestDto.getSatellites())))
                .message(decodeMessage(requestDto.getSatellites()
                        .stream()
                        .map(SateliteDto::getMessage)
                        .collect(Collectors.toList()))).build();
    }

    /**
     * @return
     */
    public MessageLocationResponseDto getLocationMessage() {
        if (sateliteLiveData.satelites.size() != 3)
            throw new NotFoundException();

        List<List<String>> messages = new ArrayList<>();

        sateliteLiveData.satelites.forEach(it -> messages.add(it.getMessage()));

        return MessageLocationResponseDto.builder()
                .message(decodeMessage(messages))
                .location(getLocation(sateliteLiveData.satelites))
                .build();
    }

    /**
     * @param sateliteDto
     */
    public void addSatelite(SateliteDto sateliteDto) {
        if (!SateliteMapper.isKnownLocation(sateliteDto.getName()))
            throw new BadRequestException("Satellite " + sateliteDto.getName() + " is unnknown");

        sateliteLiveData.satelites.removeIf(s -> s.getName().equals(sateliteDto.getName()));
        sateliteLiveData.satelites.add(SateliteMapper.map(sateliteDto));
    }

    /**
     * @param satelites
     * @return
     */
    private Location getLocation(Set<Satelite> satelites) {
        Location location = Triangulate.getIntersection(satelites);

        if (location == null)
            throw new NotFoundException();

        return location;
    }

    /**
     * @param messages
     * @return
     */
    private String decodeMessage(List<List<String>> messages) {
        String message = Decoder.decodeMessage(messages);

        if (message == null)
            throw new NotFoundException();

        return message;
    }
}
