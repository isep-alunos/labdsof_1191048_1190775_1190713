package isep.labdsof.backend.domain.requests;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {

    public String name;
    public String description;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public int maxParticipants;
    public String eventWebsite;
   /* public double latitude;
    public double longitude;
    public String street;
    public int number;
    public String postalCode;*/

}
