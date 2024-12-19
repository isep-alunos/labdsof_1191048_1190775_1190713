package isep.labdsof.backend.domain.responses.ai;

import isep.labdsof.backend.domain.responses.MessageCriticality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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

    boolean similar;
    List<Issue> issues;
    boolean created;
    @Builder.Default
    MessageCriticality criticality = MessageCriticality.WARNING;
    String message;

}
