package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.exceptions.IssueInvalidFieldException;
import isep.labdsof.backend.domain.models.BaseEntity;
import isep.labdsof.backend.domain.models.event.Event;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
public class Issue extends BaseEntity {

    private LocalDateTime creationDate;
    private String title;
    private String description;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<IssueStatusUpdate> issueStatusUpdateList;
    private IssueLocation location;
    @ManyToOne
    private Event event;

    public Issue(String title, String description, IssueLocation location, Event event) throws IssueInvalidFieldException {
        LocalDateTime now = LocalDateTime.now();
        this.creationDate = now;
        setTitle(title);
        setDescription(description);
        this.location = location;
        IssueStatusUpdate initialStatus = new IssueStatusUpdate(now,
                "Issue created",
                IssueStatus.PENDING);
        newStatusUpdate(initialStatus);
        setEvent(event);
    }

    public void setTitle(String title) throws IssueInvalidFieldException {
        if (title == null || title.isBlank()) {
            throw new IssueInvalidFieldException("Empty issue title");
        } else if (title.length() > 50) {
            throw new IssueInvalidFieldException("Issue title should not" +
                    " be greater than 50 characters");
        }
        this.title = title;
    }

    public void setDescription(String description) throws IssueInvalidFieldException {
        if (description == null || description.isBlank()) {
            throw new IssueInvalidFieldException("Empty issue description");
        } else if (description.length() > 500) {
            throw new IssueInvalidFieldException("Issue description should not" +
                    " be greater than 500 characters");
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

    public void setEvent(Event event) throws IssueInvalidFieldException {
        if (event == null) {
            throw new IssueInvalidFieldException("Invalid event");
        }
        this.event = event;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getId() != null ? getId().toString() : null);
        map.put("title", title);
        map.put("description", description);
        return map;
    }
}
