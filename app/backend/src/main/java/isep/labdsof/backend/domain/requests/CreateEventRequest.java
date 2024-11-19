package isep.labdsof.backend.domain.requests;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateEventRequest {

    public String name;
    public String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    public LocalDateTime endDate;
    public Integer maxParticipants;
    public String eventWebsite;
    public Double latitude;
    public Double longitude;
    public String street;
    public Integer number;
    public String postalCode;
    public List<UUID> eventWorkers;

}
