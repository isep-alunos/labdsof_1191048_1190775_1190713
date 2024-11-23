package isep.labdsof.backend.domain.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateIssueRequest {

    public String title;
    public String description;
    public String location;
    public String eventName;
    public boolean force;

}
