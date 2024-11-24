
package isep.labdsof.backend.domain.models.event;

import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testValidAddress() throws LabdsofCustomException {
        Address address = new Address("Main St", 123, "1234-567");
        assertNotNull(address);
        assertEquals("Main St", address.getStreet());
        assertEquals(123, address.getNumber());
        assertEquals("1234-567", address.getPostalCode());
    }

    @Test
    void testInvalidStreet() {
        assertThrows(LabdsofCustomException.class, () -> new Address(null, 123, "1234-567"));
        assertThrows(LabdsofCustomException.class, () -> new Address(" ", 123, "1234-567"));
        assertThrows(LabdsofCustomException.class, () -> new Address("This street name is way too long for an address street name".repeat(2), 123, "1234-567"));
    }

    @Test
    void testInvalidNumber() {
        assertThrows(LabdsofCustomException.class, () -> new Address("Main St", null, "1234-567"));
        assertThrows(LabdsofCustomException.class, () -> new Address("Main St", 0, "1234-567"));
        assertThrows(LabdsofCustomException.class, () -> new Address("Main St", 10000, "1234-567"));
    }

    @Test
    void testInvalidPostalCode() {
        assertThrows(LabdsofCustomException.class, () -> new Address("Main St", 123, null));
        assertThrows(LabdsofCustomException.class, () -> new Address("Main St", 123, "invalid"));
    }
}