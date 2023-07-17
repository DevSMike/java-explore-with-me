package ru.practicum.ewm.main.service.request;

import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.dto.request.RequestListDto;
import ru.practicum.ewm.main.dto.request.RequestUpdateStatusDto;

import java.util.List;

public interface RequestService {

    RequestDto createRequest(long userId, long eventId);

    List<RequestDto> getUserRequests(long userId);

    RequestDto cancelUserRequest(long userId, long requestId);

    List<RequestDto> getUserEventRequests(long userId, long eventId);

    RequestListDto changeUserEventRequestStatus(long userId, long eventId, RequestUpdateStatusDto request);

}
