package com.meli.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MessageLocationRequestDto {

    @JsonProperty("satellites")
    @NotNull
    List<SateliteDto> satellites;
}
