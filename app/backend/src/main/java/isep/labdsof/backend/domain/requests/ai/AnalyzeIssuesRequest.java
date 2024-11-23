package isep.labdsof.backend.domain.requests.ai;

import isep.labdsof.backend.domain.models.issue.Issue;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AnalyzeIssuesRequest {

    public Map<String, String> currentIssue;
    public List<Map<String, String>> pastIssues;
}
