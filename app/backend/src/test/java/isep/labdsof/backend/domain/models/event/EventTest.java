
package isep.labdsof.backend.domain.models.event;

import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testValidEvent() throws LabdsofCustomException {
        Address address = new Address("Main St", 123, "1234-567");
        EventLocation location = new EventLocation(45.0, 90.0, address);
        User user = new User(UUID.randomUUID(), "John Doe", "john@example.com", List.of(Role.EVENT_WORKER));
        Event event = new Event("Event Name", "Event Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                100, "http://example.com", location, List.of(user), null);
        assertNotNull(event);
        assertEquals("Event Name", event.getName());
        assertEquals("Event Description", event.getDescription());
        assertEquals(100, event.getMaxParticipants());
        assertEquals("http://example.com", event.getEventWebsite());
        assertEquals(location, event.getLocation());
        assertEquals(1, event.getEventWorkers().size());
    }

    @Test
    void testInvalidEventName() {
        assertThrows(LabdsofCustomException.class,
                () -> new Event(null, "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        100, "http://example.com", null, null, null));
        assertThrows(LabdsofCustomException.class,
                () -> new Event(" ", "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        100, "http://example.com", null, null, null));
        assertThrows(LabdsofCustomException.class,
                () -> new Event("This name is way too long for an event name", "Description", LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        100, "http://example.com", null, null, null));
    }

    @Test
    void testInvalidEventDescription() {
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", null, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        100, "http://example.com", null, null, null));
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", " ", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        100, "http://example.com", null, null, null));
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", "This description is way too long for an event description. ".repeat(10),
                        LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        100, "http://example.com", null, null, null));
    }

    @Test
    void testInvalidEventDates() {
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", "Description", null, LocalDateTime.now().plusDays(1),
                        100, "http://example.com", null, null, null));
        assertThrows(LabdsofCustomException.class, () -> new Event("Name", "Description", LocalDateTime.now(), null,
                100, "http://example.com", null, null, null));
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", "Description", LocalDateTime.now().plusDays(1), LocalDateTime.now(),
                        100, "http://example.com", null, null, null));
    }

    @Test
    void testInvalidMaxParticipants() {
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        null, "http://example.com", null, null, null));
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        0, "http://example.com", null, null, null));
    }

    @Test
    void testInvalidEventWebsite() {
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        100, null, null, null, null));
        assertThrows(LabdsofCustomException.class,
                () -> new Event("Name", "Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                        100, "invalid-url", null, null, null));
    }

    @Test
    void testToDto() throws LabdsofCustomException {
        Address address = new Address("Main St", 123, "1234-567");
        EventLocation location = new EventLocation(45.0, 90.0, address);
        User user = new User(UUID.randomUUID(), "John Doe", "john@example.com", List.of(Role.EVENT_WORKER));
        Event event = new Event("Event Name", "Event Description", LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                100, "http://example.com", location, List.of(user), null);
        EventDto dto = event.toDto();
        assertEquals("Event Name", dto.getName());
        assertEquals("Event Description", dto.getDescription());
        assertEquals(100, dto.getMaxParticipants());
        assertEquals("http://example.com", dto.getEventWebsite());
        assertEquals(location.toDto(), dto.getLocation());
        assertEquals(1, dto.getEventWorkerNames().size());
    }
}