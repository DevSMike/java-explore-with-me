package ru.practicum.service.request;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestListDto;
import ru.practicum.dto.request.RequestUpdateStatusDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(long userId, long eventId);

    List<RequestDto> getUserRequests(long userId);

    RequestDto cancelUserRequest(long userId, long requestId);

    List<RequestDto> getUserEventRequests(long userId, long eventId);

    RequestListDto changeUserEventRequestStatus(long userId, long eventId, RequestUpdateStatusDto request);

}
