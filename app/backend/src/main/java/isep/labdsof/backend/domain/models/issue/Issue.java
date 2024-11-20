package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.exceptions.IssueInvalidFieldException;
import isep.labdsof.backend.domain.models.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Issue extends BaseEntity {

    private LocalDateTime creationDate;
    private String title;
    private String description;

    @OneToMany
    private List<IssueStatusUpdate> issueStatusUpdateList;
    private IssueLocation location;

    public Issue(String title, String description, IssueLocation location) throws IssueInvalidFieldException {
        LocalDateTime now = LocalDateTime.now();
        this.creationDate = now;
        setTitle(title);
        setDescription(description);
        this.location = location;
        IssueStatusUpdate initialStatus = new IssueStatusUpdate(now,
                "Issue created",
                IssueStatus.PENDING);
        newStatusUpdate(initialStatus);
    }

    public void setTitle(String title) throws IssueInvalidFieldException {
        if (title == null || title.isBlank()) {
            throw new IssueInvalidFieldException("Empty issue title");
        } else if (title.length() > 20) {
            throw new IssueInvalidFieldException("Issue title should not" +
                    " be greater than 20 characters");
        }
        this.title = title;
    }

    public void setDescription(String description) throws IssueInvalidFieldException {
        if (description == null || description.isBlank()) {
            throw new IssueInvalidFieldException("Empty issue description");
        } else if (description.length() > 200) {
            throw new IssueInvalidFieldException("Issue description should not" +
                    " be greater than 200 characters");
        }
        this.description = description;
    }

    public void newStatusUpdate(IssueStatusUpdate newStatus) throws IssueInvalidFieldException {
        if (issueStatusUpdateList == null) {
            issueStatusUpdateList = new ArrayList<>();
        }
        if (newStatus == null) {
            throw new IssueInvalidFieldException("Invalid new status update");
        }
        issueStatusUpdateList.add(newStatus);
    }
}
