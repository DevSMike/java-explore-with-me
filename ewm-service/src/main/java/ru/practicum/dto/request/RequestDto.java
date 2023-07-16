package ru.practicum.dto.request;

import ru.practicum.model.request.RequestStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class RequestDto {

    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private RequestStatus status;
}
