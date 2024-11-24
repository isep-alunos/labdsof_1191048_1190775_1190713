
package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestBasicDto;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApproveRoleRequestUserNotFound() {
        final RoleRequestBasicDto request = new RoleRequestBasicDto("nonexistent@example.com", "EVENT_WORKER");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        final StatusResponse result = adminService.approveRoleRequest(request);

        assertEquals(false, result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertEquals("User not found", result.getMessages().get(0).getMessage());
    }

    @Test
    void testApproveRoleRequestRoleNotFound() {
        final String email = "test@example.com";
        final RoleRequestBasicDto request = new RoleRequestBasicDto(email, "INVALID_ROLE");

        final User user = User.builder().email(email).build();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        final StatusResponse result = adminService.approveRoleRequest(request);

        assertEquals(false, result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertEquals("Role not found", result.getMessages().get(0).getMessage());
    }

    @Test
    void testApproveRoleRequestEventWorker() {
        final String email = "test@example.com";
        final RoleRequestBasicDto request = new RoleRequestBasicDto(email, "EVENT_WORKER");

        final User user = User.builder()
                .email(email)
                .isWaitingForApprovalForEventWorker(true)
                .isWaitingForApprovalForEventManager(true)
                .roles(List.of(Role.USER))
                .build();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final StatusResponse result = adminService.approveRoleRequest(request);

        assertEquals("Event Worker role approved", result.getMessages().get(0).getMessage());
        assertEquals("Event Worker role added", result.getMessages().get(1).getMessage());
        assertEquals(true, result.isSuccess());
        assertEquals(2, result.getMessages().size());
    }

    @Test
    void testRejectRoleRequestUserNotFound() {
        final RoleRequestBasicDto request = new RoleRequestBasicDto("nonexistent@example.com", "EVENT_WORKER");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        final StatusResponse result = adminService.rejectRoleRequest(request);

        assertEquals(false, result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertEquals("User not found", result.getMessages().get(0).getMessage());
    }

    @Test
    void testRejectRoleRequestEventWorker() {
        final String email = "test@example.com";
        final RoleRequestBasicDto request = new RoleRequestBasicDto(email, "EVENT_WORKER");

        final User user = User.builder().email(email).isWaitingForApprovalForEventWorker(true).build();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        final StatusResponse result = adminService.rejectRoleRequest(request);

        assertEquals("Event Worker role rejected. User was removed from the database.", result.getMessages().get(0).getMessage());
        assertEquals(true, result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }
}