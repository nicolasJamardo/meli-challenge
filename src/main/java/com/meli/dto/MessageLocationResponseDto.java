package com.meli.dto;

import com.meli.model.Location;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MessageLocationResponseDto {
    private Location location;
    private String message;
}