package isep.labdsof.backend.domain.dtos.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventDto {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipants;
    private String eventWebsite;
    private EventLocationDto location;
    private List<String> eventWorkerNames;
}