package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.dtos.issue.IssueLocationDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;

@Embeddable
public class IssueLocation extends ValueObject {

    private String location;

    public IssueLocation(String location) throws LabdsofCustomException {
        setLocation(location);
    }

    public IssueLocation() {

    }

    public void setLocation(String location) throws LabdsofCustomException {
        if (location == null || location.isBlank()) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Empty issue location");
        } else if (location.length() > 100) {
            throw new LabdsofCustomException(AppCustomExceptions.ISSUE_INVALID_FIELD, "Issue location should not" +
                    " be greater than 100 characters");
        }
        this.location = location;
    }

    public IssueLocationDto toDto() {
        return IssueLocationDto.builder()
                .location(location)
                .build();
    }
}
