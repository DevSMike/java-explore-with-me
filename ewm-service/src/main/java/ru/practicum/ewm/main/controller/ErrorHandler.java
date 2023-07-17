package ru.practicum.ewm.main.controller;

import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.exception.ForbiddenDataException;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.ServletRequestBindingException;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        log.error("Error: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("An exception throws")
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }

    @ExceptionHandler(IncorrectDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectDataException(IncorrectDataException e) {
        log.error("400: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }

    @ExceptionHandler(ConflictDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictDataException(ConflictDataException e) {
        log.error("409: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoDataException.class)
    public ApiError handleNoDataException(NoDataException e) {
        log.error("404: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        log.error("400: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("The validation by annotation is not passed.")
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletRequestBindingException.class)
    public ApiError handleServletRequestBindingException(ServletRequestBindingException e) {
        log.error("400: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("The validation by annotation in RequestParam is not passed.")
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenDataException.class)
    public ApiError handleForbiddenDataException(ForbiddenDataException e) {
        log.error("409: {}", e.getMessage());
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .errors(Collections.singletonList(stackTrace))
                .build();
    }


}
