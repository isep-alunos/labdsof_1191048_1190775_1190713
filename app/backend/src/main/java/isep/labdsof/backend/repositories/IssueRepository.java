package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.issue.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {

}
