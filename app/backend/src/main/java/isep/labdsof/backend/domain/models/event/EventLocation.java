package isep.labdsof.backend.domain.models.event;


import isep.labdsof.backend.domain.exceptions.event.EventInvalidFieldException;
import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class EventLocation extends ValueObject {

    private Double latitude;
    private Double longitude;
    private Address address;

    public EventLocation(Double latitude, Double longitude, Address address) throws EventInvalidFieldException {
        setLatitude(latitude);
        setLongitude(longitude);
        setAddress(address);
    }

    public void setLatitude(Double latitude) throws EventInvalidFieldException {
        if (latitude == null) {
            throw new EventInvalidFieldException("Empty latitude");
        } else if (!(latitude >= -90 && latitude <= 90)) {
            throw new EventInvalidFieldException("Latitude hould be >= -90 and <= 90");
        }

        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) throws EventInvalidFieldException {
        if (longitude == null) {
            throw new EventInvalidFieldException("Empty longitude");
        } else if (!(longitude >= -180 && longitude <= 180)) {
            throw new EventInvalidFieldException("Longitude hould be >= -180 and <= 180");
        }
        this.longitude = longitude;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
