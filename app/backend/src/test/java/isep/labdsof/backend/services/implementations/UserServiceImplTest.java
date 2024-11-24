
package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetById_UserExists() throws LabdsofCustomException {
        final UUID userId = UUID.randomUUID();
        final User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final User result = userService.getById(userId);

        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetById_UserNotFound() {
        final UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(LabdsofCustomException.class, () -> userService.getById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetEventWorkers() {
        final User user = new User();
        user.setRoles(List.of(Role.EVENT_WORKER));
        when(userRepository.findAllByRolesContaining(Role.EVENT_WORKER)).thenReturn(List.of(user));

        final List<User> result = userService.getEventWorkers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userRepository).findAllByRolesContaining(Role.EVENT_WORKER);
    }
}