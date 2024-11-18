package isep.labdsof.backend.services;


import isep.labdsof.backend.domain.models.user.UserProfile;

import java.util.UUID;

public interface UserProfileService {

    UserProfile getByUserId(UUID id) throws Exception;

    UserProfile getByUserEmail(String userEmail) throws Exception;
}
