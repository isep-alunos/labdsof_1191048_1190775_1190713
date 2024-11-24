package isep.labdsof.backend.domain.models.event;


import isep.labdsof.backend.domain.dtos.event.EventLocationDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class EventLocation extends ValueObject {

    private Double latitude;
    private Double longitude;
    private Address address;

    public EventLocation(Double latitude, Double longitude, Address address) throws LabdsofCustomException {
        setLatitude(latitude);
        setLongitude(longitude);
        setAddress(address);
    }

    public void setLatitude(Double latitude) throws LabdsofCustomException {
        if (latitude == null) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty latitude");
        } else if (!(latitude >= -90 && latitude <= 90)) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Latitude hould be >= -90 and <= 90");
        }

        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) throws LabdsofCustomException {
        if (longitude == null) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty longitude");
        } else if (!(longitude >= -180 && longitude <= 180)) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Longitude hould be >= -180 and <= 180");
        }
        this.longitude = longitude;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public EventLocationDto toDto() {
        return EventLocationDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .street(address.getStreet())
                .number(address.getNumber())
                .postalCode(address.getPostalCode())
                .build();
    }
}
