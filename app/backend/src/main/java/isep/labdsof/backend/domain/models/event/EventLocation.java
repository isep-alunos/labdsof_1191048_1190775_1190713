package isep.labdsof.backend.domain.models.event;


import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;

@Embeddable
public class EventLocation extends ValueObject {

    private double latitude;
    private double longitude;
    private Address address;

}
