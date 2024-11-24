package isep.labdsof.backend.domain.requests.ai;

import isep.labdsof.backend.domain.responses.MessageCriticality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyzeIssuesResponse {


    @Data
    @AllArgsConstructor
    public static class Issue {
        String id;
        String title;
        String description;
    }

    private boolean similar;
    private int count;
    private Issue[] issues;
    private boolean created;
    @Builder.Default
    private MessageCriticality criticality = MessageCriticality.WARNING;
    private String message;
}
