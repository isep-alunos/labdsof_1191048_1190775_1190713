package isep.labdsof.backend.domain.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzeIssuesResponse {


    @Data
    @AllArgsConstructor
    public static class Issue {
        String id;
        String title;
    }

    boolean similar;
    int count;
    Issue[] issues;
    boolean created;
    MessageCriticality criticality = MessageCriticality.WARNING;

}
