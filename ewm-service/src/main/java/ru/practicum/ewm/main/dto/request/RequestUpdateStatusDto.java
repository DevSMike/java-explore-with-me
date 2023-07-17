package ru.practicum.ewm.main.dto.request;

import ru.practicum.ewm.main.model.request.RequestStatus;
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
