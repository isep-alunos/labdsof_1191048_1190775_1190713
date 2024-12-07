package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.BaseEntity;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Issue extends BaseEntity {

    @ManyToOne
    private User userReporter;

    private LocalDateTime creationDate;
    @Getter
    private String title;
    @Getter
    private String description;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<IssueStatusUpdate> issueStatusUpdateList;
    private IssueLocation location;
    @ManyToOne
    private Event event;

    @ManyToMany
    private Set<User> reactions;

    public Issue(User user, String title, String description, IssueLocation location, Event event) throws LabdsofCustomException {
        setUserReporter(user);
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
        setReactions();
    }

    public void setUserReporter(final User userReporter) throws LabdsofCustomException {
        if (userReporter == null) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Invalid user reporter");
        }
        this.userReporter = userReporter;
    }

    public void setTitle(String title) throws LabdsofCustomException {
        if (title == null || title.isBlank()) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Empty issue title");
        } else if (title.length() > 50) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Issue title should not" +
                    " be greater than 50 characters");
        }
        this.title = title;
    }

    public void setDescription(String description) throws LabdsofCustomException {
        if (description == null || description.isBlank()) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Empty issue description");
        } else if (description.length() > 500) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Issue description should not" +
                    " be greater than 500 characters");
        }
        this.description = description;
    }

    public void newStatusUpdate(final IssueStatusUpdate newStatus) throws LabdsofCustomException {
        if (issueStatusUpdateList == null) {
            issueStatusUpdateList = new ArrayList<>();
        }
        if (newStatus == null) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Invalid new status update");
        }
        issueStatusUpdateList.add(newStatus);
    }

    public void setEvent(Event event) throws LabdsofCustomException {
        if (event == null) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Invalid event");
        }
        this.event = event;
    }

    @SafeVarargs
    public final void setReactions(final Set<User>... reactions) {
        if (reactions == null || reactions.length == 0) {
            this.reactions = new HashSet<>();
        } else {
            this.reactions = new HashSet<>(reactions[0]);
        }
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getId() != null ? getId().toString() : null);
        map.put("title", title);
        map.put("description", description);
        return map;
    }

    public IssueDto toDto(final String userEmail) {
        return IssueDto.builder()
                .id(String.valueOf(getId()))
                .creationDate(creationDate)
                .title(title)
                .description(description)
                .issueStatusUpdateList(issueStatusUpdateList.stream()
                        .map(IssueStatusUpdate::toDto) // Convert IssueStatusUpdate to its DTO
                        .toList())
                .location(location.toDto()) // Assuming IssueLocation has a toDto method
                .eventName(event.getName()) // If you only need the Event ID, otherwise map Event to EventDto
                .reactions(reactions.size())
                .userReacted(reactions.stream().anyMatch(u -> u.getEmail().equals(userEmail)))
                .userIsOwner(userReporter.getEmail().equals(userEmail))
                .build();
    }

    public void addReaction(final User user) throws LabdsofCustomException {
        if (reactions == null) {
            reactions = new HashSet<>();
        }
        if (reactions.contains(user)) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "User already reacted to this issue");
        }
        reactions.add(user);
    }
}
