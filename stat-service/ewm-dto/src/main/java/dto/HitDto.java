package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class HitDto {

    private String uri;
    private String ip;
    private String timestamp;
    private String app;
}
