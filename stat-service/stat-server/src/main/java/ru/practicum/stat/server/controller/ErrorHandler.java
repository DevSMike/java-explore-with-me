package ru.practicum.stat.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stat.server.exception.IncorrectDataException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(IncorrectDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIncorrectDataException(IncorrectDataException e) {
        log.error("400: {}", e.getMessage());
        return "IncorrectDataException:" + e.getMessage();
    }

}
