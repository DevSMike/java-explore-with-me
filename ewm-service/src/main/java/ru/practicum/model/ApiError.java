package ru.practicum.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Builder
public class ApiError {

    private HttpStatus status;
    private String reason;
    private String message;
    private List<String> errors;
}
