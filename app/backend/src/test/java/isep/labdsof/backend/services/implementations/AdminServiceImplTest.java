
package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestBasicDto;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private static final String roleAppealResultEmailTitle = "Role Appeal Result";
    private static final String roleAppealResultEmailBody = """
Dear %s,

Your registration appeal for the role "%s" has been reviewed. The result of your appeal is as follows:

Result: %s

If you have any questions or require further assistance, please do not hesitate to contact our support team.

Best regards,
Porto Eventos
""";

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

        verifyNoInteractions(emailService);
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

        verifyNoInteractions(emailService);
    }

    @Test
    void testApproveRoleRequestEventWorker() {
        final String email = "test@example.com";
        final RoleRequestBasicDto request = new RoleRequestBasicDto(email, "EVENT_WORKER");

        final User user = User.builder()
                .email(email)
                .nome("Jose Test")
                .isWaitingForApprovalForEventWorker(true)
                .isWaitingForApprovalForEventManager(true)
                .roles(List.of(Role.USER))
                .build();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final StatusResponse result = adminService.approveRoleRequest(request);

        assertEquals(true, result.isSuccess());
        assertEquals(2, result.getMessages().size());
        assertEquals("Event Worker role approved", result.getMessages().get(0).getMessage());
        assertEquals("Event Worker role added", result.getMessages().get(1).getMessage());

        verify(emailService).sendMail(
                email,
                roleAppealResultEmailTitle,
                roleAppealResultEmailBody.formatted("Jose Test", "EVENT_WORKER", "APPROVED"));
    }

    @Test
    void testRejectRoleRequestUserNotFound() {
        final RoleRequestBasicDto request = new RoleRequestBasicDto("nonexistent@example.com", "EVENT_WORKER");

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        final StatusResponse result = adminService.rejectRoleRequest(request);

        assertEquals(false, result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertEquals("User not found", result.getMessages().get(0).getMessage());

        verifyNoInteractions(emailService);
    }

    @Test
    void testRejectRoleRequestEventWorker() {
        final String email = "test@example.com";
        final RoleRequestBasicDto request = new RoleRequestBasicDto(email, "EVENT_WORKER");

        final User user = User.builder().email(email).nome("Jose Test").isWaitingForApprovalForEventWorker(true).build();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        final StatusResponse result = adminService.rejectRoleRequest(request);

        assertEquals(true, result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertEquals("Event Worker role rejected. User was removed from the database.", result.getMessages().get(0).getMessage());

        verify(userProfileRepository).deleteUserProfileByUser(user);
        verify(userRepository).delete(user);
        verify(emailService).sendMail(
                email,
                roleAppealResultEmailTitle,
                roleAppealResultEmailBody.formatted("Jose Test", "EVENT_WORKER", "REJECTED"));
    }
}