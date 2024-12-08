package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    Optional<Event> findByName(String name);

    @Query("SELECT e FROM Event e JOIN e.eventWorkers w WHERE w.email = :email")
    List<Event> findAllByWorkerEmail(@Param("email") final String email);
}
