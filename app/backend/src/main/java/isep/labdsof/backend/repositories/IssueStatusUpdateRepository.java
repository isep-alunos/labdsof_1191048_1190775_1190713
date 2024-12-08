package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.issue.IssueStatusUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IssueStatusUpdateRepository extends JpaRepository<IssueStatusUpdate, UUID> {

}
