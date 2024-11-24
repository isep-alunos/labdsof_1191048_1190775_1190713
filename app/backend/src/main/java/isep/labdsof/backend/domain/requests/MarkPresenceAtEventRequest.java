package isep.labdsof.backend.domain.requests;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MarkPresenceAtEventRequest {

    @NotNull(message = "Event name is required")
    @NotEmpty(message = "Event name is required")
    @Size(max = 20, message = "Event name can't be greater than 20 characters")
    private String name;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @NotNull(message = "Longitude is required")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @NotNull(message = "Longitude is required")
    private Double longitude;
}
