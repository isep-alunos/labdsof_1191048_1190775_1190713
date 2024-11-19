package isep.labdsof.backend.services.implementations;


import isep.labdsof.backend.domain.exceptions.UserNotFoundException;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile getByUserId(UUID id) throws Exception {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(id);
        if (userProfileOpt.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return userProfileOpt.get();
    }

    @Override
    public UserProfile getByUserEmail(String email) throws Exception {
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUser_Email(email);
        if (userProfileOpt.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        return userProfileOpt.get();
    }
}