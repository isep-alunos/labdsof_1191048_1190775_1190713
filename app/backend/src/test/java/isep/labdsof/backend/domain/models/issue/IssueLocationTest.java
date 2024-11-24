
package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IssueLocationTest {

    private final String location = "Valid location";

    @Test
    void testSetLocation() {
        IssueLocation issueLocation = new IssueLocation();
        assertThrows(LabdsofCustomException.class, () -> issueLocation.setLocation(null));
        assertThrows(LabdsofCustomException.class, () -> issueLocation.setLocation(""));
        assertThrows(LabdsofCustomException.class, () -> issueLocation.setLocation("a".repeat(101)));
        assertDoesNotThrow(() -> issueLocation.setLocation(location));
    }
}