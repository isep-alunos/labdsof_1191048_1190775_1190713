package isep.labdsof.backend.domain.models.event;


import isep.labdsof.backend.domain.exceptions.EventInvalidFieldException;
import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Address extends ValueObject {

    private String street;
    private Integer number;
    private String postalCode;

    public Address(String street, Integer number, String postalCode) throws EventInvalidFieldException {
        setStreet(street);
        setNumber(number);
        setPostalCode(postalCode);
    }

    public void setStreet(String street) throws EventInvalidFieldException {
        if (street == null || street.trim().isEmpty()) {
            throw new EventInvalidFieldException("Street cannot be null or empty");
        }
        if (street.length() > 100) {
            throw new EventInvalidFieldException("Street should not be greater than 100 characters");
        }
        this.street = street.trim();
    }

    public void setNumber(Integer number) throws EventInvalidFieldException {
        if (number == null) {
            throw new EventInvalidFieldException("Empty street number");
        }
        if (number <= 0 || number > 9999) {
            throw new EventInvalidFieldException("Number must be a positive integer and less than 9999");
        }
        this.number = number;
    }

    public void setPostalCode(String postalCode) throws EventInvalidFieldException {
        if (postalCode == null || !postalCode.matches("\\d{4}-\\d{3}")) {
            throw new EventInvalidFieldException("Postal code must follow the format NNNN-NNN");
        }
        this.postalCode = postalCode;
    }
}
