package ru.practicum.exception;

public class ForbiddenDataException extends RuntimeException {

    public ForbiddenDataException(String message) {
        super(message);
    }
}
