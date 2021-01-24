package com.meli.error_handler;

import com.meli.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.swing.text.html.Option;
import java.util.Optional;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException exception) {
        LOGGER.error("NotFoundException", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException exception) {
        LOGGER.error("BadRequestException", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleMessageNotReadableException(HttpMessageNotReadableException exception) {
        LOGGER.error("HttpMessageNotReadableException", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(Optional.ofNullable(exception.getMessage())
                .orElse("")
                .split("\n")[0]));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleMessageNotReadableException(Exception exception) {
        LOGGER.error("Exception", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
