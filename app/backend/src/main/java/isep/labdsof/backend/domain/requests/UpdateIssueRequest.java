package isep.labdsof.backend.domain.requests;

import isep.labdsof.backend.domain.models.issue.IssueStatus;
import lombok.Data;

@Data
public class UpdateIssueRequest {
    private String issueId;
    private IssueStatus status;
    private String description;
    private boolean isAssigned;
}
