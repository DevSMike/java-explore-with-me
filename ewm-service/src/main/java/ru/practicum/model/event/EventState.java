package ru.practicum.model.event;

import ru.practicum.exception.IncorrectDataException;

public enum EventState {

    PENDING,
    PUBLISHED,
    CANCELED;

    public static EventState converToEventState(String state) {
        try {
            return EventState.valueOf(state);
        } catch (Exception e) {
            throw new IncorrectDataException("State is incorrect");
        }
    }
}
