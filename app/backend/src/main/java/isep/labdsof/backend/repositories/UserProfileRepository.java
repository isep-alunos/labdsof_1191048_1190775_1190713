package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    @Override
    void delete(UserProfile userProfile);

    void deleteUserProfileByUser(User user);

    Optional<UserProfile> findByUserId(UUID user_id);
    
    Optional<UserProfile> findByUser_Email(String user_email);
}
