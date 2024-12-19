package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.domain.responses.LeaderboardEntryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    @Override
    void delete(UserProfile userProfile);

    void deleteUserProfileByUser(User user);

    Optional<UserProfile> findByUserId(UUID user_id);
    
    Optional<UserProfile> findByUser_Email(String user_email);

    @Query("SELECT new isep.labdsof.backend.domain.responses.LeaderboardEntryDto(u.user.nome, u.pointsAvailable) FROM UserProfile u WHERE u.pointsAvailable > 0 ORDER BY u.pointsAvailable DESC")
    List<LeaderboardEntryDto> getTopMostPointsAvailable(Pageable pageable);
}
