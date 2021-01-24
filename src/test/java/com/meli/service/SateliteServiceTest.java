package com.meli.service;

import com.meli.data.SateliteLiveData;
import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.SateliteDto;
import com.meli.error_handler.BadRequestException;
import com.meli.error_handler.NotFoundException;
import com.meli.model.Location;
import com.meli.model.Satelite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SateliteServiceTest {

    private SateliteLiveData sateliteLiveData;
    private SateliteService sateliteService;

    @BeforeEach
    public void setup() {
        sateliteLiveData = new SateliteLiveData();
        sateliteService = new SateliteService(sateliteLiveData);
    }

    @Test
    public void getLocationMessageWithTwoValidSatellites_WhenGettingLocationMessage_ThenThrowBadRequestException() throws BadRequestException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithTwoSatellites();

        assertThrows(BadRequestException.class, () -> sateliteService.getLocationMessage(messageLocationRequestDto));
    }

    @Test
    public void getLocationMessageWithTwoValidSatellitesButOneInvalid_WhenGettingLocationMessage_ThenThrowBadRequestException() throws BadRequestException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithTwoSatellitesAndOneInvalid();

        assertThrows(BadRequestException.class, () -> sateliteService.getLocationMessage(messageLocationRequestDto));
    }

    @Test
    public void getLocationMessageWithThreeValidSatellitesButNoIntersection_WhenGettingLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithThreeValidSatellitesButNoIntersection();

        assertThrows(NotFoundException.class, () -> sateliteService.getLocationMessage(messageLocationRequestDto));
    }

    @Test
    public void getLocationMessageWithThreeValidSatellitesButNoDecodableMessage_WhenGettingLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithThreeValidSatellitesButNoDecodableMessage();

        assertThrows(NotFoundException.class, () -> sateliteService.getLocationMessage(messageLocationRequestDto));
    }


    @Test
    public void getLocationMessageWithValidSatellites_WhenGettingLocationMessage_ThenLocationAndMessageAreReturned() {
        MessageLocationRequestDto messageLocationRequestDto = getMessageLocationRequestDtoWithThreeValidSatellites();

        MessageLocationResponseDto messageLocationResponseDto = sateliteService.getLocationMessage(messageLocationRequestDto);

        assertNotNull(messageLocationResponseDto.getLocation());
        assertEquals(-400.0, messageLocationResponseDto.getLocation().getX());
        assertEquals(200.0, messageLocationResponseDto.getLocation().getY());
        assertNotNull(messageLocationResponseDto.getMessage());
        assertEquals("este es un mensaje", messageLocationResponseDto.getMessage());
    }



    @Test
    public void getLocationMessageWithNoPreviousSatellite_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> sateliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithOnePreviousValidSatellite_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        addOneValidSatellite();
        assertThrows(NotFoundException.class, () -> sateliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithTwoPreviousValidSatellites_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException {
        addTwoValidSatellites();
        assertThrows(NotFoundException.class, () -> sateliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithThreePreviousValidSatellites_WhenGetLocationMessage_ThenMessageIsReturned() {
        addThreeValidSatellites();

        MessageLocationResponseDto messageLocationResponseDto = sateliteService.getLocationMessage();

        assertNotNull(messageLocationResponseDto.getLocation());
        assertEquals(-400.0, messageLocationResponseDto.getLocation().getX());
        assertEquals(200.0, messageLocationResponseDto.getLocation().getY());
        assertNotNull(messageLocationResponseDto.getMessage());
        assertEquals("este es un mensaje", messageLocationResponseDto.getMessage());
    }

    @Test
    public void getLocationMessageWithThreePreviousValidSatellitesButInvalidLocations_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException{
        addThreeValidSatellitesButInvalidLocations();

        assertThrows(NotFoundException.class, () -> sateliteService.getLocationMessage());
    }

    @Test
    public void getLocationMessageWithThreePreviousValidSatellitesButNoDecodableMessage_WhenGetLocationMessage_ThenThrowNotFoundException() throws NotFoundException{
        addThreeValidSatellitesButNoDecodableMessage();

        assertThrows(NotFoundException.class, () -> sateliteService.getLocationMessage());
    }

    @Test
    public void addSateliteWithInvalidSatellite_WhenAddSatellite_ThenThrowBadRequestException() throws BadRequestException{
        SateliteDto sateliteDto = getInvalidSatelliteDto();
        assertThrows(BadRequestException.class, () -> sateliteService.addSatelite(sateliteDto));
    }

    @Test
    public void addSateliteWitValidSatellite_WhenAddSatellite_ThenAccept() throws BadRequestException{
        SateliteDto sateliteDto = getValidSatelliteDto ();
        assertAll(()->sateliteService.addSatelite(sateliteDto));
    }

    private SateliteDto getInvalidSatelliteDto() {
        return new SateliteDto("obiwan", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        )));
    }

    private SateliteDto getValidSatelliteDto() {
        return new SateliteDto("kenobi", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        )));
    }

    private void addThreeValidSatellitesButNoDecodableMessage() {
        sateliteLiveData.satelites.add(new Satelite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "error"
        ))));
        sateliteLiveData.satelites.add(new Satelite("skywalker", 583.0951895, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        sateliteLiveData.satelites.add(new Satelite("sato", 905.5385138, new Location(500, 100), new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
    }

    private void addThreeValidSatellitesButInvalidLocations() {
        sateliteLiveData.satelites.add(new Satelite("kenobi", 1000, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        sateliteLiveData.satelites.add(new Satelite("skywalker", 5, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        sateliteLiveData.satelites.add(new Satelite("sato", 1, new Location(500, 100), new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
    }


    private void addOneValidSatellite() {
        sateliteLiveData.satelites.add(new Satelite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
    }

    private void addThreeValidSatellites() {
        sateliteLiveData.satelites.add(new Satelite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        sateliteLiveData.satelites.add(new Satelite("skywalker", 583.0951895, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        sateliteLiveData.satelites.add(new Satelite("sato", 905.5385138, new Location(500, 100), new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
    }

    private void addTwoValidSatellites() {
        sateliteLiveData.satelites.add(new Satelite("kenobi", 412.3105626, new Location(-500, -200), new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        sateliteLiveData.satelites.add(new Satelite("skywalker", 583.0951895, new Location(100, -100), new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
    }


    private MessageLocationRequestDto getMessageLocationRequestDtoWithTwoSatellites() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SateliteDto> sateliteDtoList = new ArrayList<>();
        sateliteDtoList.add(new SateliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        sateliteDtoList.add(new SateliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(sateliteDtoList);
        return requestDto;
    }

    private MessageLocationRequestDto getMessageLocationRequestDtoWithTwoSatellitesAndOneInvalid() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SateliteDto> sateliteDtoList = new ArrayList<>();
        sateliteDtoList.add(new SateliteDto("obiwan", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        sateliteDtoList.add(new SateliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        sateliteDtoList.add(new SateliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(sateliteDtoList);
        return requestDto;
    }

    private MessageLocationRequestDto getMessageLocationRequestDtoWithThreeValidSatellites() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SateliteDto> sateliteDtoList = new ArrayList<>();
        sateliteDtoList.add(new SateliteDto("kenobi", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        sateliteDtoList.add(new SateliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        sateliteDtoList.add(new SateliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(sateliteDtoList);
        return requestDto;
    }

    private MessageLocationRequestDto getMessageLocationRequestDtoWithThreeValidSatellitesButNoIntersection() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SateliteDto> sateliteDtoList = new ArrayList<>();
        sateliteDtoList.add(new SateliteDto("kenobi", 10000, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        ))));
        sateliteDtoList.add(new SateliteDto("skywalker", 1, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                "mensaje"
        ))));
        sateliteDtoList.add(new SateliteDto("sato", 2, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(sateliteDtoList);
        return requestDto;
    }
    private MessageLocationRequestDto getMessageLocationRequestDtoWithThreeValidSatellitesButNoDecodableMessage() {
        MessageLocationRequestDto requestDto = new MessageLocationRequestDto();
        List<SateliteDto> sateliteDtoList = new ArrayList<>();
        sateliteDtoList.add(new SateliteDto("kenobi", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "error"
        ))));
        sateliteDtoList.add(new SateliteDto("skywalker", 583.0951895, new ArrayList<>(Arrays.asList(
                "este",
                "",
                "un",
                null
        ))));
        sateliteDtoList.add(new SateliteDto("sato", 905.5385138, new ArrayList<>(Arrays.asList(
                "",
                "",
                "es",
                "",
                "mensaje"
        ))));
        requestDto.setSatellites(sateliteDtoList);
        return requestDto;

    }
}