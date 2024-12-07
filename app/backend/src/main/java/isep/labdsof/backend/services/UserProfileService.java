package isep.labdsof.backend.services;


import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;

import java.util.UUID;

public interface UserProfileService {

    UserProfile getByUserId(UUID id) throws LabdsofCustomException;

    UserProfile getByUserEmail(String userEmail) throws LabdsofCustomException;
}
