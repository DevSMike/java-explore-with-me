package ru.practicum.ewm.main.dto.request;

import ru.practicum.ewm.main.model.request.RequestStatus;
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
