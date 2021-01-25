package com.meli.controller;

import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.RecievedMessageDto;
import com.meli.dto.SatelliteDto;
import com.meli.service.SatelliteService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SatelliteControllerTest {

    private SatelliteController satelliteController;
    private SatelliteService satelliteService;

    @Before
    public void setup() {
        satelliteService = mock(SatelliteService.class);
        satelliteController = new SatelliteController(satelliteService);
    }

    @Test
    public void testTopSecretSplitPost_WhenValidDto_ThenResponseIsOk() {
        SatelliteDto satelliteDto = getValidSatelliteDto();
        RecievedMessageDto recievedMessageDto = new RecievedMessageDto();
        doNothing().when(satelliteService)
                .addSatellite(satelliteDto);

        ResponseEntity<?> response = satelliteController.addSatellite("kenobi", recievedMessageDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testTopSecret_WhenValidDto_ThenResponseIsOk() {
        MessageLocationRequestDto messageLocationRequestDto = getValidMessageLocationRequestDto();
        doReturn(mock(MessageLocationResponseDto.class))
                .when(satelliteService)
                .getLocationMessage(messageLocationRequestDto);

        ResponseEntity<MessageLocationResponseDto> response = satelliteController.topsecret(messageLocationRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(satelliteService).getLocationMessage(messageLocationRequestDto);
    }

    @Test
    public void testTopSecretSplitGet_WhenValidDto_ThenResponseIsOk() {
        MessageLocationRequestDto messageLocationRequestDto = getValidMessageLocationRequestDto();
        doReturn(mock(MessageLocationResponseDto.class))
                .when(satelliteService)
                .getLocationMessage();

        ResponseEntity<MessageLocationResponseDto> response = satelliteController.getStoredLocation();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    private MessageLocationRequestDto getValidMessageLocationRequestDto() {
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

    private SatelliteDto getValidSatelliteDto() {
        return new SatelliteDto("kenobi", 412.3105626, new ArrayList<>(Arrays.asList(
                "",
                "este",
                "es",
                "un",
                "mensaje"
        )));
    }

}
