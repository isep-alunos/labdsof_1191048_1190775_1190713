package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {

    Optional<Event> findByName(String name);
}
