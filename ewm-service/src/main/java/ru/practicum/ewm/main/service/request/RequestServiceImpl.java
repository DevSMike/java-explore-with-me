package ru.practicum.ewm.main.service.request;

import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.dto.request.RequestListDto;
import ru.practicum.ewm.main.dto.request.RequestUpdateStatusDto;
import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.exception.ForbiddenDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.RequestMapper;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.model.event.EventState;
import ru.practicum.ewm.main.model.request.Request;
import ru.practicum.ewm.main.model.request.RequestStatus;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.RequestRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestDto createRequest(long userId, long eventId) {
        if (requestRepository.findUserRequestToEvent(eventId, userId).isPresent()) {
            throw new ConflictDataException("Field: request. Error: request уже существует");
        }
        Event eventFromDb = findEventById(eventId);
        User userFromDb = findUserById(userId);

        if (eventFromDb.getInitiator().getId() == userId) {
            throw new ConflictDataException("Field: request. Error: инициатор не может пойти на своё мероприятие");
        }
        if (!eventFromDb.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Field: request. Error: нельзя учавствовать в неопубликованном событии");
        }
        if ((eventFromDb.getConfirmedRequests() >= eventFromDb.getParticipantLimit())
                && (eventFromDb.getConfirmedRequests() != 0 && eventFromDb.getParticipantLimit() != 0)) {
            throw new ConflictDataException("Field: request. Error: у события достигнут максимум участников");
        }

        RequestStatus status;
        if (!eventFromDb.getIsRequestModeration() || eventFromDb.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
            eventFromDb.setConfirmedRequests(eventFromDb.getConfirmedRequests() + 1);
        } else {
            status = RequestStatus.PENDING;
        }
        Request request = RequestMapper.toRequest(eventFromDb, userFromDb, status);
        requestRepository.save(request);
        eventRepository.save(eventFromDb);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getUserRequests(long userId) {
        findUserById(userId);
        List<Request> result = requestRepository.findUserRequests(userId);
        return result.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto cancelUserRequest(long userId, long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NoDataException("Request with id = " + requestId + " was not found"));
        if (userId != request.getRequester().getId()) {
            throw new ForbiddenDataException("Field: request. Error: пользователь отменяет не свой запрос");
        }
        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> getUserEventRequests(long userId, long eventId) {
        findUserById(userId);
        findEventById(eventId);
        List<Request> result = requestRepository.findRequestsToEvent(eventId);
        return result.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestListDto changeUserEventRequestStatus(long userId, long eventId, RequestUpdateStatusDto request) {
        findUserById(userId);
        Event userEvent = findEventById(eventId);

        if (userId != userEvent.getInitiator().getId()) {
            throw new ConflictDataException("Field: request. Error: пользователь не инициатор события");
        }
        if (userEvent.getConfirmedRequests() >= userEvent.getParticipantLimit()) {
            throw new ConflictDataException("Field: request. Error: у события достигнут максимум участников");
        }
        if (!userEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictDataException("Field: request. Error: нельзя принимать приглашение в неопуликованном событии");
        }

        List<Request> requests = requestRepository.findRequestsByIds(request.getRequestIds());
        List<RequestDto> confirmedRequest = new ArrayList<>();
        List<RequestDto> rejectedRequest = new ArrayList<>();

        for (Request r : requests) {
            if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                if (userEvent.getParticipantLimit() == 0 || !userEvent.getIsRequestModeration()) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequest.add(RequestMapper.toRequestDto(r));
                } else if (userEvent.getConfirmedRequests() < userEvent.getParticipantLimit()) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    userEvent.setConfirmedRequests(userEvent.getConfirmedRequests() + 1);
                    confirmedRequest.add(RequestMapper.toRequestDto(r));
                } else {
                    r.setStatus(RequestStatus.REJECTED);
                    rejectedRequest.add(RequestMapper.toRequestDto(r));
                }
            } else {
                r.setStatus(RequestStatus.REJECTED);
                rejectedRequest.add(RequestMapper.toRequestDto(r));
            }
            requestRepository.save(r);
        }

        eventRepository.save(userEvent);

        return RequestListDto.builder()
                .confirmedRequests(confirmedRequest)
                .rejectedRequests(rejectedRequest)
                .build();
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoDataException("User with id = " + userId + " was not found"));
    }

    private Event findEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NoDataException("Event with id = " + eventId + " was not found"));
    }
}
