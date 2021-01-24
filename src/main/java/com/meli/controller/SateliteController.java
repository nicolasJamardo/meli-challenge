package com.meli.controller;

import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.RecievedMessageDto;
import com.meli.dto.SateliteDto;
import com.meli.service.SateliteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Api(tags = "satelite", description = "Satelite related operations")
@RestController
public class SateliteController {

    private SateliteService sateliteService;

    @Autowired
    public SateliteController(SateliteService sateliteService) {
        this.sateliteService = sateliteService;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(SateliteController.class);

    @RequestMapping(method = RequestMethod.POST, value = "/topsecret")
    @ApiOperation(value = "Gets location and message from sender", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(message = "Location of the ship and message", code = 200, response = MessageLocationResponseDto.class, responseContainer = "MessageLocationResponseDto"),
            @ApiResponse(message = "Not found", code = 404)
    })
    public ResponseEntity<MessageLocationResponseDto> topsecret(@RequestBody @Valid MessageLocationRequestDto requestDto) {
        LOGGER.trace("topsecret - gets location and message for the satelite located to the following distances = [{}}", requestDto);
        MessageLocationResponseDto messageLocationResponseDto = sateliteService.getLocationMessage(requestDto);

        return ok(messageLocationResponseDto);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/topsecret_split/{satelite_name}")
    @ApiOperation(value = "Adds a satelite", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(message = "Satelite added correctly", code = 200),
            @ApiResponse(message = "Not found", code = 404)
    })
    public ResponseEntity<?> addSatelite(@PathVariable("satelite_name") String sateliteName,
                                         @RequestBody @Valid RecievedMessageDto recievedMessageDto) {
        LOGGER.trace("addSatelite - adds the satelite called = [{}}", sateliteName);

        sateliteService.addSatelite(new SateliteDto(sateliteName, recievedMessageDto.getDistance(), recievedMessageDto.getMessage()));
        return notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/topsecret_split")
    @ApiOperation(value = "Gets location and message from sender", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(message = "Location of the ship and message", code = 200, response = MessageLocationResponseDto.class, responseContainer = "MessageLocationResponseDto"),
            @ApiResponse(message = "Not found", code = 404)
    })
    public ResponseEntity<MessageLocationResponseDto> getStoredLocation() {
        LOGGER.trace("getStoredLocation - gets location and message for the satelites previously sent");

        MessageLocationResponseDto messageLocationResponseDto = sateliteService.getLocationMessage();
        return ok(messageLocationResponseDto);
    }


}
