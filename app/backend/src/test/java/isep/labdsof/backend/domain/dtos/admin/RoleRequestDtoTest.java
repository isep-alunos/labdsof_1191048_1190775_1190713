
package isep.labdsof.backend.domain.dtos.admin;

import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoleRequestDtoTest {

    @Test
    void testFromUser() {
        final User user = new User(UUID.randomUUID(), "john@example.com", "John Doe", List.of(Role.USER));
        user.setWaitingForApprovalForEventWorker(true);
        user.setEventWorkerJustification("Justification for event worker");
        user.setWaitingForApprovalForEventManager(true);
        user.setEventManagerJustification("Justification for event manager");

        final List<RoleRequestDto> roleRequests = RoleRequestDto.fromUser(user);
        assertEquals(2, roleRequests.size());

        final RoleRequestDto eventWorkerRequest = roleRequests.get(0);
        assertEquals("John Doe", eventWorkerRequest.getName());
        assertEquals("john@example.com", eventWorkerRequest.getEmail());
        assertEquals(Role.EVENT_WORKER.getShortRoleDescription(), eventWorkerRequest.getRole());
        assertEquals("Justification for event worker", eventWorkerRequest.getJustification());

        final RoleRequestDto eventManagerRequest = roleRequests.get(1);
        assertEquals("John Doe", eventManagerRequest.getName());
        assertEquals("john@example.com", eventManagerRequest.getEmail());
        assertEquals(Role.EVENT_MANAGER.getShortRoleDescription(), eventManagerRequest.getRole());
        assertEquals("Justification for event manager", eventManagerRequest.getJustification());
    }
}