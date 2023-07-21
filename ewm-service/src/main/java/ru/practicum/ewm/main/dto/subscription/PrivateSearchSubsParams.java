package ru.practicum.ewm.main.dto.subscription;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class PrivateSearchSubsParams {

    @NotNull
    private long subId;
    private long initId;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    private int from;
    private int size;
    private String text;
    private String sort;
}
