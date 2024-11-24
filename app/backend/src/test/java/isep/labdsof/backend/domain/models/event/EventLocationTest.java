
package isep.labdsof.backend.domain.models.event;

import isep.labdsof.backend.domain.dtos.event.EventLocationDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventLocationTest {

    @Test
    void testValidEventLocation() throws LabdsofCustomException {
        Address address = new Address("Main St", 123, "1234-567");
        EventLocation location = new EventLocation(45.0, 90.0, address);
        assertNotNull(location);
        assertEquals(45.0, location.getLatitude());
        assertEquals(90.0, location.getLongitude());
        assertEquals(address, location.getAddress());
    }

    @Test
    void testInvalidLatitude() throws LabdsofCustomException {
        Address address = new Address("Main St", 123, "1234-567");
        assertThrows(LabdsofCustomException.class, () -> new EventLocation(null, 90.0, address));
        assertThrows(LabdsofCustomException.class, () -> new EventLocation(100.0, 90.0, address));
    }

    @Test
    void testInvalidLongitude() throws LabdsofCustomException {
        Address address = new Address("Main St", 123, "1234-567");
        assertThrows(LabdsofCustomException.class, () -> new EventLocation(45.0, null, address));
        assertThrows(LabdsofCustomException.class, () -> new EventLocation(45.0, 200.0, address));
    }

    @Test
    void testToDto() throws LabdsofCustomException {
        Address address = new Address("Main St", 123, "1234-567");
        EventLocation location = new EventLocation(45.0, 90.0, address);
        EventLocationDto dto = location.toDto();
        assertEquals(45.0, dto.getLatitude());
        assertEquals(90.0, dto.getLongitude());
        assertEquals("Main St", dto.getStreet());
        assertEquals(123, dto.getNumber());
        assertEquals("1234-567", dto.getPostalCode());
    }
}