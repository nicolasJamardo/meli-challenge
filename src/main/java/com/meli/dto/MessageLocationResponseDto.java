package com.meli.dto;

import com.meli.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MessageLocationResponseDto {

    private Location location;
    private String message;
}