package isep.labdsof.backend.domain.requests;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MarkPresenceAtEventRequest {

    @NotNull(message = "Event name is required")
    @NotEmpty(message = "Event name is required")
    @Size(max = 20, message = "Event name must be greater than 20 characters")
    public String name;

}
