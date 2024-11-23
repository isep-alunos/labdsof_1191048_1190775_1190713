package isep.labdsof.backend.domain.models.issue;

import jakarta.persistence.Embeddable;

@Embeddable
public enum IssueStatus {
    PENDING, IN_PROGRESS, RESOLVED, REJECTED;
}
