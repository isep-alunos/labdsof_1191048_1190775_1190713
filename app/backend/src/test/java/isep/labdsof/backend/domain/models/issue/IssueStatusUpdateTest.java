
package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IssueStatusUpdateTest {

    private final LocalDateTime now = LocalDateTime.now();
    private final String description = "Status update description";
    private final IssueStatus status = IssueStatus.PENDING;

    @Test
    void testSetUpdateTime() {
        IssueStatusUpdate issueStatusUpdate = new IssueStatusUpdate();
        assertThrows(LabdsofCustomException.class, () -> issueStatusUpdate.setUpdateTime(null));
        assertDoesNotThrow(() -> issueStatusUpdate.setUpdateTime(now));
    }

    @Test
    void testSetDescription() {
        IssueStatusUpdate issueStatusUpdate = new IssueStatusUpdate();
        assertThrows(LabdsofCustomException.class, () -> issueStatusUpdate.setDescription(null));
        assertThrows(LabdsofCustomException.class, () -> issueStatusUpdate.setDescription(""));
        assertThrows(LabdsofCustomException.class, () -> issueStatusUpdate.setDescription("a".repeat(201)));
        assertDoesNotThrow(() -> issueStatusUpdate.setDescription(description));
    }

    @Test
    void testSetStatus() {
        IssueStatusUpdate issueStatusUpdate = new IssueStatusUpdate();
        assertThrows(LabdsofCustomException.class, () -> issueStatusUpdate.setStatus(null));
        assertDoesNotThrow(() -> issueStatusUpdate.setStatus(status));
    }
}