package com.meli.service;

import com.meli.data.SatelliteLiveData;
import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.SatelliteDto;
import com.meli.error_handler.BadRequestException;
import com.meli.error_handler.NotFoundException;
import com.meli.model.Location;
import com.meli.model.Satellite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SatelliteServiceTest {

    private SatelliteLiveData satelliteLiveData;
    private SatelliteService satelliteService;

    @BeforeEach
    public void setup() {
        satelliteLiveData = new SatelliteLiveData();
        satelliteService = new SatelliteService(satelliteLiveData);
    }

    @Test
    public void getLocationMessageWithTwoValidSatellites_WhenGettingLocationMessage_ThenThrowBadRequestException() throws BadRequestException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithTwoSatellites();

        assertThrows(BadRequestException.class, () -> satelliteService.getLocationMessage(messageLocationRequestDto));
    }

    @Test
    public void getLocationMessageWithTwoValidSatellitesButOneInvalid_WhenGettingLocationMessage_ThenThrowBadRequestException() throws BadRequestException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithTwoSatellitesAndOneInvalid();

        assertThrows(BadRequestException.class, () -> satelliteService.getLocationMessage(messageLocationRequestDto));
    }

    @Test
    public void getLocationMessageWithThreeValidSatellitesButNoIntersection_WhenGettingLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithThreeValidSatellitesButNoIntersection();

        assertThrows(NotFoundException.class, () -> satelliteService.getLocationMessage(messageLocationRequestDto));
    }

    @Test
    public void getLocationMessageWithThreeValidSatellitesButNoDecodableMessage_WhenGettingLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithThreeValidSatellitesButNoDecodableMessage();

        assertThrows(NotFoundException.class, () -> satelliteService.getLocationMessage(messageLocationRequestDto));
    }


    @Test
    public void getLocationMessageWithValidSatellites_WhenGettingLocationMessage_ThenLocationAndMessageAreReturned() {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithThreeValidSatellites();

        MessageLocationResponseDto messageLocationResponseDto = satelliteService.getLocationMessage(messageLocationRequestDto);

        assertNotNull(messageLocationResponseDto.getLocation());
        assertEquals(-400.0, messageLocationResponseDto.getLocation().getX());
        assertEquals(200.0, messageLocationResponseDto.getLocation().getY());
        assertNotNull(messageLocationResponseDto.getMessage());
        assertEquals("este es un mensaje", messageLocationResponseDto.getMessage());
    }


    @Test
    public void getLocationMessageWithNoPreviousSatellite_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> satelliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithOnePreviousValidSatellite_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        addOneValidSatellite();
        assertThrows(NotFoundException.class, () -> satelliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithTwoPreviousValidSatellites_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        addTwoValidSatellites();
        assertThrows(NotFoundException.class, () -> satelliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithThreePreviousValidSatellites_WhenGetLocationMessage_ThenMessageIsReturned() {
        addThreeValidSatellites();

        MessageLocationResponseDto messageLocationResponseDto = satelliteService.getLocationMessage();

        assertNotNull(messageLocationResponseDto.getLocation());
        assertEquals(-400.0, messageLocationResponseDto.getLocation().getX());
        assertEquals(200.0, messageLocationResponseDto.getLocation().getY());
        assertNotNull(messageLocationResponseDto.getMessage());
        assertEquals("este es un mensaje", messageLocationResponseDto.getMessage());
    }

    @Test
    public void getLocationMessageWithThreePreviousValidSatellitesButInvalidLocations_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        addThreeValidSatellitesButInvalidLocations();

        assertThrows(NotFoundException.class, () -> satelliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithThreePreviousValidSatellitesButNoDecodableMessage_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        addThreeValidSatellitesButNoDecodableMessage();

        assertThrows(NotFoundException.class, () -> satelliteService.getLocationMessage());
    }

    @Test
    public void addSatelliteWithInvalidSatellite_WhenAddSatellite_ThenThrowBadRequestException() throws BadRequestException {
        SatelliteDto satelliteDto = getInvalidSatelliteDto();
        assertThrows(BadRequestException.class, () -> satelliteService.addSatellite(satelliteDto));
    }

    @Test
    public void addSatelliteWitValidSatellite_WhenAddSatellite_ThenAccept() throws BadRequestException {
        SatelliteDto satelliteDto = getValidSatelliteDto();
        assertAll(() -> satelliteService.addSatellite(satelliteDto));
    }

    private SatelliteDto getInvalidSatelliteDto() {
        return new SatelliteDto("obiwan", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        )));
    }

    private SatelliteDto getValidSatelliteDto() {
        return new SatelliteDto("kenobi", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        )));
    }

    private void addThreeValidSatellitesButNoDecodableMessage() {
        satelliteLiveData.satellites.add(new Satellite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "error"
        ))));
        satelliteLiveData.satellites.add(new Satellite("skywalker", 583.0951895, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        satelliteLiveData.satellites.add(new Satellite("sato", 905.5385138, new Location(500, 100), new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
    }

    private void addThreeValidSatellitesButInvalidLocations() {
        satelliteLiveData.satellites.add(new Satellite("kenobi", 1000, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        satelliteLiveData.satellites.add(new Satellite("skywalker", 5, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        satelliteLiveData.satellites.add(new Satellite("sato", 1, new Location(500, 100), new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
    }


    private void addOneValidSatellite() {
        satelliteLiveData.satellites.add(new Satellite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
    }

    private void addThreeValidSatellites() {
        satelliteLiveData.satellites.add(new Satellite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        satelliteLiveData.satellites.add(new Satellite("skywalker", 583.0951895, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        satelliteLiveData.satellites.add(new Satellite("sato", 905.5385138, new Location(500, 100), new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
    }

    private void addTwoValidSatellites() {
        satelliteLiveData.satellites.add(new Satellite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        satelliteLiveData.satellites.add(new Satellite("skywalker", 583.0951895, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
    }


    private MessageLocationRequestDto getMessageLocationRequestDtoWithTwoSatellites() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SatelliteDto> satelliteDtoList = new ArrayList<>();
        satelliteDtoList.add(new SatelliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        satelliteDtoList.add(new SatelliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(satelliteDtoList);
        return requestDto;
    }

    private MessageLocationRequestDto getMessageLocationRequestDtoWithTwoSatellitesAndOneInvalid() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SatelliteDto> satelliteDtoList = new ArrayList<>();
        satelliteDtoList.add(new SatelliteDto("obiwan", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        satelliteDtoList.add(new SatelliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        satelliteDtoList.add(new SatelliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(satelliteDtoList);
        return requestDto;
    }

    private MessageLocationRequestDto getMessageLocationRequestDtoWithThreeValidSatellites() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SatelliteDto> satelliteDtoList = new ArrayList<>();
        satelliteDtoList.add(new SatelliteDto("kenobi", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        satelliteDtoList.add(new SatelliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        satelliteDtoList.add(new SatelliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(satelliteDtoList);
        return requestDto;
    }

    private MessageLocationRequestDto getMessageLocationRequestDtoWithThreeValidSatellitesButNoIntersection() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SatelliteDto> satelliteDtoList = new ArrayList<>();
        satelliteDtoList.add(new SatelliteDto("kenobi", 10000, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        satelliteDtoList.add(new SatelliteDto("skywalker", 1, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        satelliteDtoList.add(new SatelliteDto("sato", 2, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(satelliteDtoList);
        return requestDto;
    }

    private MessageLocationRequestDto getMessageLocationRequestDtoWithThreeValidSatellitesButNoDecodableMessage() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SatelliteDto> satelliteDtoList = new ArrayList<>();
        satelliteDtoList.add(new SatelliteDto("kenobi", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "error"
        ))));
        satelliteDtoList.add(new SatelliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                null
        ))));
        satelliteDtoList.add(new SatelliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(satelliteDtoList);
        return requestDto;

    }
}