package isep.labdsof.backend.domain.models.issue;

import isep.labdsof.backend.domain.exceptions.IssueInvalidFieldException;
import isep.labdsof.backend.domain.models.ValueObject;
import jakarta.persistence.Embeddable;

@Embeddable
public class IssueLocation extends ValueObject {

    private String location;

    public IssueLocation(String location) throws IssueInvalidFieldException {
        setLocation(location);
    }

    public IssueLocation() {

    }

    public void setLocation(String location) throws IssueInvalidFieldException {
        if (location == null || location.isBlank()) {
            throw new IssueInvalidFieldException("Empty issue location");
        } else if (location.length() > 100) {
            throw new IssueInvalidFieldException("Issue location should not" +
                    " be greater than 100 characters");
        }
        this.location = location;
    }
}
