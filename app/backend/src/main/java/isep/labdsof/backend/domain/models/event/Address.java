package isep.labdsof.backend.domain.models.event;


import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address extends ValueObject {

    private String street;
    private int number;
    private String postalCode;

}
