package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Override
    void delete(User user);

    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isWaitingForApprovalForEventWorker OR u.isWaitingForApprovalForEventManager")
    List<User> findAllUsersWaitingForApproval();
}
