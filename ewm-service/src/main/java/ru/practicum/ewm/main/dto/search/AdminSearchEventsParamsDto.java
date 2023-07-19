package ru.practicum.ewm.main.dto.search;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class AdminSearchEventsParamsDto {

    private List<Long> userIds;
    private List<Long> categoriesIds;
    private List<String> states;
    private String rangeStart;
    private String rangeEnd;
    private int from;
    private int size;

}
