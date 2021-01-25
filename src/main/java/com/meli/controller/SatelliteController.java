package com.meli.controller;

import com.meli.dto.MessageLocationRequestDto;
import com.meli.dto.MessageLocationResponseDto;
import com.meli.dto.RecievedMessageDto;
import com.meli.dto.SatelliteDto;
import com.meli.service.SatelliteService;
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

@Api(tags = "satellite", description = "Satellite related operations")
@RestController
public class SatelliteController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SatelliteController.class);
    private SatelliteService satelliteService;

    @Autowired
    public SatelliteController(SatelliteService satelliteService) {
        this.satelliteService = satelliteService;
    }

    /**
     * Calculates the location and decoded message from a ship that was sent to three specific satellites
     * @param requestDto - a list of satellites with their specific name, distance to the ship and message recieved
     * @return MessageLocationResponseDto - the location of the ship and the decoded message
     */
    @RequestMapping(method = RequestMethod.POST, value = "/topsecret")
    @ApiOperation(value = "Gets location and message from sender", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(message = "Location of the ship and message", code = 200, response = MessageLocationResponseDto.class, responseContainer = "MessageLocationResponseDto"),
            @ApiResponse(message = "Not found", code = 404)
    })
    public ResponseEntity<MessageLocationResponseDto> topsecret(@RequestBody @Valid MessageLocationRequestDto requestDto) {
        LOGGER.trace("topsecret - gets location and message for the satellite located to the following distances = [{}}", requestDto);
        MessageLocationResponseDto messageLocationResponseDto = satelliteService.getLocationMessage(requestDto);

        return ok(messageLocationResponseDto);
    }

    /**
     * Adds one specific satellite
     * @param satelliteName - the name of the satellite being added
     * @param recievedMessageDto - the message recieved by the satellite
     * @return not found in case the satellite was correctly added
     */
    @RequestMapping(method = RequestMethod.POST, value = "/topsecret_split/{satellite_name}")
    @ApiOperation(value = "Adds a satellite", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(message = "Satellite added correctly", code = 200),
            @ApiResponse(message = "Not found", code = 404)
    })
    public ResponseEntity<?> addSatellite(@PathVariable("satellite_name") String satelliteName,
                                         @RequestBody @Valid RecievedMessageDto recievedMessageDto) {
        LOGGER.trace("addSatellite - adds the satellite called = [{}}", satelliteName);

        satelliteService.addSatellite(new SatelliteDto(satelliteName, recievedMessageDto.getDistance(), recievedMessageDto.getMessage()));
        return ok().build();
    }

    /**
     * Gets the location of the ship and the decoded message calculated from the previously stored satellites
     * @return messageLocationResponseDto - the location of the ship and the decoded message
     */
    @RequestMapping(method = RequestMethod.GET, value = "/topsecret_split")
    @ApiOperation(value = "Gets location and message from sender", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(message = "Location of the ship and message", code = 200, response = MessageLocationResponseDto.class, responseContainer = "MessageLocationResponseDto"),
            @ApiResponse(message = "Not found", code = 404)
    })
    public ResponseEntity<MessageLocationResponseDto> getStoredLocation() {
        LOGGER.trace("getStoredLocation - gets location and message for the satellites previously sent");

        MessageLocationResponseDto messageLocationResponseDto = satelliteService.getLocationMessage();
        return ok(messageLocationResponseDto);
    }
}
