package isep.labdsof.backend.domain.requests;

import lombok.Data;

@Data
public class AnalyseIssueRequest {

    public String title;
    public String description;
    public String location;
    public String eventName;

}
