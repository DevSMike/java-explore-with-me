package ru.practicum.ewm.main.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.request.Request;
import ru.practicum.ewm.main.model.request.RequestStatus;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
