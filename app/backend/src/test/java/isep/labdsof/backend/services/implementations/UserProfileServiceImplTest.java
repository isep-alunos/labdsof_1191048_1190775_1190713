
package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.repositories.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void testGetByUserId_UserProfileExists() throws LabdsofCustomException {
        final UUID userId = UUID.randomUUID();
        final UserProfile userProfile = new UserProfile();
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.of(userProfile));

        final UserProfile result = userProfileService.getByUserId(userId);

        assertEquals(userProfile, result);
        verify(userProfileRepository).findByUserId(userId);
    }

    @Test
    void testGetByUserId_UserProfileNotFound() {
        final UUID userId = UUID.randomUUID();
        when(userProfileRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(LabdsofCustomException.class, () -> userProfileService.getByUserId(userId));
        verify(userProfileRepository).findByUserId(userId);
    }

    @Test
    void testGetByUserEmail_UserProfileExists() throws LabdsofCustomException {
        final String email = "test@example.com";
        final UserProfile userProfile = new UserProfile();
        when(userProfileRepository.findByUser_Email(email)).thenReturn(Optional.of(userProfile));

        final UserProfile result = userProfileService.getByUserEmail(email);

        assertEquals(userProfile, result);
        verify(userProfileRepository).findByUser_Email(email);
    }

    @Test
    void testGetByUserEmail_UserProfileNotFound() {
        final String email = "test@example.com";
        when(userProfileRepository.findByUser_Email(email)).thenReturn(Optional.empty());

        assertThrows(LabdsofCustomException.class, () -> userProfileService.getByUserEmail(email));
        verify(userProfileRepository).findByUser_Email(email);
    }
}