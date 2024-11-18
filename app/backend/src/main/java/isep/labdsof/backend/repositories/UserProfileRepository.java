package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    @Override
    void delete(UserProfile userProfile);

    Optional<UserProfile> findByUserId(UUID user_id);
    
    Optional<UserProfile> findByUser_Email(String user_email);
}
