
package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.RegisterUserDto;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        final String email = "test@example.com";
        final String name = "Test User";
        final boolean isEventWorker = false;
        final String eventWorkerInfo = "";
        final boolean isEventManager = false;
        final String eventManagerInfo = "";

        final User existingUser = User.builder().email(email).build();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(existingUser));

        final RegisterUserDto result = authService.register(email, name, isEventWorker, eventWorkerInfo, isEventManager, eventManagerInfo);

        assertEquals(HttpStatus.BAD_REQUEST, result.getErrorStatus());
        assertEquals(1, result.getMessages().size());
        assertEquals("User already exists", result.getMessages().get(0).getMessage());
    }

    @Test
    void testRegisterNewUser() {
        final String email = "newuser@example.com";
        final String name = "New User";
        final boolean isEventWorker = true;
        final String eventWorkerInfo = "Event Worker Info";
        final boolean isEventManager = true;
        final String eventManagerInfo = "Event Manager Info";

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final RegisterUserDto result = authService.register(email, name, isEventWorker, eventWorkerInfo, isEventManager, eventManagerInfo);

        assertEquals(true, result.isUserCreated());
        assertEquals(3, result.getMessages().size());
        assertEquals("User registered successfully.", result.getMessages().get(0).getMessage());
        assertEquals("Waiting for approval to be an Event Worker", result.getMessages().get(1).getMessage());
        assertEquals("Waiting for approval to be an Event Manager", result.getMessages().get(2).getMessage());
    }
}