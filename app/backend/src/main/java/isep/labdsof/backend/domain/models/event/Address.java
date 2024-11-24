package isep.labdsof.backend.domain.models.event;

import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Address extends ValueObject {

    private String street;
    private Integer number;
    private String postalCode;

    public Address(String street, Integer number, String postalCode) throws LabdsofCustomException {
        setStreet(street);
        setNumber(number);
        setPostalCode(postalCode);
    }

    public void setStreet(String street) throws LabdsofCustomException {
        if (street == null || street.trim().isEmpty()) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Street cannot be null or empty");
        }
        if (street.length() > 100) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Street should not be greater than 100 characters");
        }
        this.street = street.trim();
    }

    public void setNumber(Integer number) throws LabdsofCustomException {
        if (number == null) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty street number");
        }
        if (number <= 0 || number > 9999) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Number must be a positive integer and less than 9999");
        }
        this.number = number;
    }

    public void setPostalCode(String postalCode) throws LabdsofCustomException {
        if (postalCode == null || !postalCode.matches("\\d{4}-\\d{3}")) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Postal code must follow the format NNNN-NNN");
        }
        this.postalCode = postalCode;
    }
}
