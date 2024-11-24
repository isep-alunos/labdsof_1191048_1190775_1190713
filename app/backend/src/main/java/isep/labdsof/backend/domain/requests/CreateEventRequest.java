package isep.labdsof.backend.domain.requests;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateEventRequest {

    private String name;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    private Integer maxParticipants;
    private String eventWebsite;
    private Double latitude;
    private Double longitude;
    private String street;
    private Integer number;
    private String postalCode;
    private List<UUID> eventWorkers;

}
