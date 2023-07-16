package ru.practicum.mapper;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;

import java.time.LocalDateTime;

public class RequestMapper {

    public static Request toRequest(Event event, User requester, RequestStatus status) {
        return Request.builder()
                .created(LocalDateTime.now())
                .status(status)
                .requester(requester)
                .event(event)
                .build();
    }

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .created(EventMapper.toStringFromDate(request.getCreated()))
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .id(request.getId())
                .status(request.getStatus())
                .build();
    }
}
