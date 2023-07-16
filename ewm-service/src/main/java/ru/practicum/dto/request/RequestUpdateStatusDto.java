package ru.practicum.dto.request;

import ru.practicum.model.request.RequestStatus;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class RequestUpdateStatusDto {

    private List<Long> requestIds;
    private RequestStatus status;
}
