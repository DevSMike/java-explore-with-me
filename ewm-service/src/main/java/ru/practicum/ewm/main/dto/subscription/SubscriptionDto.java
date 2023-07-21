package ru.practicum.ewm.main.dto.subscription;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class SubscriptionDto {

    private Long id;
    @NotNull
    private Long subId;
    @NotNull
    private Long initId;
    @NotNull
    private Boolean isSub;
    private LocalDateTime timestamp;

}
