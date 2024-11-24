
package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IssueTest {

    private Issue issue;
    private final String title = "Issue title";
    private final String description = "Issue description";
    private final IssueLocation location = new IssueLocation("Location");
    private final Event event = new Event();

    IssueTest() throws LabdsofCustomException {
    }

    @BeforeEach
    void setUp() throws LabdsofCustomException {
        issue = new Issue(title, description, location, event);
    }

    @Test
    void testSetTitle() {
        assertThrows(LabdsofCustomException.class, () -> issue.setTitle(null));
        assertThrows(LabdsofCustomException.class, () -> issue.setTitle(""));
        assertThrows(LabdsofCustomException.class, () -> issue.setTitle("a".repeat(51)));
        assertDoesNotThrow(() -> issue.setTitle(title));
    }

    @Test
    void testSetDescription() {
        assertThrows(LabdsofCustomException.class, () -> issue.setDescription(null));
        assertThrows(LabdsofCustomException.class, () -> issue.setDescription(""));
        assertThrows(LabdsofCustomException.class, () -> issue.setDescription("a".repeat(501)));
        assertDoesNotThrow(() -> issue.setDescription(description));
    }

    @Test
    void testNewStatusUpdate() throws LabdsofCustomException {
        IssueStatusUpdate newStatus = new IssueStatusUpdate(LocalDateTime.now(), "New status", IssueStatus.PENDING);
        assertDoesNotThrow(() -> issue.newStatusUpdate(newStatus));
        assertThrows(LabdsofCustomException.class, () -> issue.newStatusUpdate(null));
    }

    @Test
    void testSetEvent() {
        assertThrows(LabdsofCustomException.class, () -> issue.setEvent(null));
        assertDoesNotThrow(() -> issue.setEvent(event));
    }
}