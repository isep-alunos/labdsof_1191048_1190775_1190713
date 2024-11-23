package isep.labdsof.backend.domain.requests.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    boolean similar;
    int count;
    Issue[] issues;
    boolean created;
    MessageCriticality criticality = MessageCriticality.WARNING;
    String message;

}
