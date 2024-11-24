
package isep.labdsof.backend.domain.models.userProfile;

import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    private UserProfile userProfile;
    private final User user = new User();
    private final Event event = new Event();

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile(UUID.randomUUID(), user);
    }

    @Test
    void testAddAttendedEvent() throws LabdsofCustomException {
        assertTrue(userProfile.addAttendedEvent(event));
        assertThrows(LabdsofCustomException.class, () -> userProfile.addAttendedEvent(event));
    }
}