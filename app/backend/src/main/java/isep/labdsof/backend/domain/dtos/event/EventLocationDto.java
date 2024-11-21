package isep.labdsof.backend.domain.dtos.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventLocationDto {
    private Double latitude;
    private Double longitude;
    private String street;
    private Integer number;
    private String postalCode;
}