package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.dtos.issue.IssueStatusUpdateDto;
import isep.labdsof.backend.domain.exceptions.IssueInvalidFieldException;
import isep.labdsof.backend.domain.models.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Entity
public class IssueStatusUpdate extends BaseEntity {

    private LocalDateTime updateTime;

    private String description;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    public IssueStatusUpdate(LocalDateTime updateTime, String description, IssueStatus status) throws IssueInvalidFieldException {
        setUpdateTime(updateTime);
        setDescription(description);
        setStatus(status);
    }

    public IssueStatusUpdate() {

    }

    public void setUpdateTime(LocalDateTime updateTime) throws IssueInvalidFieldException {
        if (updateTime == null) {
            throw new IssueInvalidFieldException("Invalid update time");
        }
        this.updateTime = updateTime;
    }

    public void setDescription(String description) throws IssueInvalidFieldException {
        if (description == null || description.isBlank()) {
            throw new IssueInvalidFieldException("Empty status update description");
        } else if (description.length() > 200) {
            throw new IssueInvalidFieldException("Status update description should not" +
                    " be greater than 200 characters");
        }
        this.description = description;
    }

    public void setStatus(IssueStatus status) throws IssueInvalidFieldException {
        if (status == null) {
            throw new IssueInvalidFieldException("Invalid new status");
        }
        this.status = status;
    }

    public IssueStatusUpdateDto toDto() {
        return IssueStatusUpdateDto.builder()
                .updateTime(updateTime)
                .description(description)
                .status(status != null ? status.name() : IssueStatus.PENDING.name()) // The status will be passed as the Enum (or you can map to a DTO if needed)
                .build();
    }
}
