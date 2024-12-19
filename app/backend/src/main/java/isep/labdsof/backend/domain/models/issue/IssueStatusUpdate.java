package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.dtos.issue.IssueStatusUpdateDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class IssueStatusUpdate extends BaseEntity {

    private LocalDateTime updateTime;

    private String description;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    public IssueStatusUpdate(LocalDateTime updateTime, String description, IssueStatus status) throws LabdsofCustomException {
        setUpdateTime(updateTime);
        setDescription(description);
        setStatus(status);
    }

    public IssueStatusUpdate() {

    }

    public void setUpdateTime(LocalDateTime updateTime) throws LabdsofCustomException {
        if (updateTime == null) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Invalid update time");
        }
        this.updateTime = updateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(IssueStatus status) throws LabdsofCustomException {
        if (status == null) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Invalid new status");
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
