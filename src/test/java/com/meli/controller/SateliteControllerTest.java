package com.meli.controller;

import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.RecievedMessageDto;
import com.meli.dto.SateliteDto;
import com.meli.service.SateliteService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SateliteControllerTest {

    private SateliteController sateliteController;
    private SateliteService sateliteService;

    @Before
    public void setup() {
        sateliteService = mock(SateliteService.class);
        sateliteController = new SateliteController(sateliteService);
    }

    @Test
    public void testTopSecret_WhenValidDto_ThenResponseIsOk() {
        MessageLocationRequestDto messageLocationRequestDto = getValidMessageLocationRequestDto();
        doReturn(mock(MessageLocationResponseDto.class))
                .when(sateliteService)
                .getLocationMessage(messageLocationRequestDto);

        ResponseEntity<MessageLocationResponseDto> response = sateliteController.topsecret(messageLocationRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sateliteService).getLocationMessage(messageLocationRequestDto);
    }

//    @Test
//    public void testAddSatelite_WhenValidDto_ThenResponseIsOk() {
//        RecievedMessageDto recievedMessageDto = getValidRecievedMessageDto();
//        doReturn(mock(MessageLocationResponseDto.class))
//                .when(sateliteService)
//                .addSatelite(getValidRecievedMessageDto());
//
//        ResponseEntity<MessageLocationResponseDto> response = sateliteController.topsecret(get);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(sateliteService).getLocationMessage(messageLocationRequestDto);
//    }
//
//    private RecievedMessageDto getValidRecievedMessageDto() {
//
//    }

    private MessageLocationRequestDto getValidMessageLocationRequestDto() {
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

    private MessageLocationRequestDto getInvalidMessageLocationRequestDto() {
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
}
