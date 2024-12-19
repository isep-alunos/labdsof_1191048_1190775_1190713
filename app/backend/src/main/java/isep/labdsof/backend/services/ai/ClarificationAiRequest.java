package isep.labdsof.backend.services.ai;

import isep.labdsof.backend.domain.models.issue.Issue;

import isep.labdsof.backend.domain.requests.ai.ClarificationRequest;
import isep.labdsof.backend.domain.responses.ai.ClarificationResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ClarificationAiRequest implements AiRequest {

    final Issue issue;
    ClarificationResponse response;

    public ClarificationAiRequest(Issue issue) {
        this.issue = issue;
    }

    @Override
    public HttpEntity createHttpEntity() {
        ClarificationRequest requestPayload = new ClarificationRequest();
        requestPayload.setCurrentIssue(issue.toMap());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestPayload, headers);
    }

    @Override
    public String getEndpoint() {
        return "generate_clarification";
    }

    @Override
    public void readResponse(String response) {
        if (response == null) {
            this.response = null;
            return;
        }

        try {
            String[] questions = response.split(";");
            List<String> questionsList = Arrays.stream(questions).map(String::trim).toList();
            this.response = ClarificationResponse.builder()
                    .questions(questionsList)
                    .build();
        } catch (Exception e) {
            this.response = null;
        }

    }

    public ClarificationResponse getResponse() {
        return response;
    }
}
