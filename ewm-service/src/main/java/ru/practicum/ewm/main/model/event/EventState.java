package ru.practicum.ewm.main.model.event;

import ru.practicum.ewm.main.exception.IncorrectDataException;

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
