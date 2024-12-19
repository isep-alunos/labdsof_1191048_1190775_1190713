package isep.labdsof.backend.domain.requests.ai;

import lombok.Data;

import java.util.Map;

@Data
public class ClarificationRequest {

    public Map<String, String> currentIssue;
}
