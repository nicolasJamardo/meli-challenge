package com.meli.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @RequestMapping(method = RequestMethod.GET, value = "/ping")
    @ApiOperation(value = "Gets pong", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(message = "pong", code = 200),
    })
    public String ping() {
        return "pong";
    }
}
