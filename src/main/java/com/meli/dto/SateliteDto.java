package com.meli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SateliteDto {

    @JsonProperty("name")
    String name;

    @JsonProperty("distance")
    double distance;

    @JsonProperty("message")
    List<String> message;
}
