package isep.labdsof.backend.services.ai;

import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesRequest;
import isep.labdsof.backend.domain.responses.ai.AnalyzeIssuesResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnalyseIssuesAiRequest implements AiRequest {

    final Issue issue;
    final List<Issue> pastIssues;
    AnalyzeIssuesResponse response;

    public AnalyseIssuesAiRequest(Issue issue, List<Issue> pastIssues) {
        this.issue = issue;
        this.pastIssues = pastIssues;
    }

    @Override
    public HttpEntity createHttpEntity() {
        AnalyzeIssuesRequest requestPayload = new AnalyzeIssuesRequest();
        requestPayload.setCurrentIssue(issue.toMap());
        requestPayload.setPastIssues(pastIssues.stream().map(Issue::toMap).toList());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestPayload, headers);
    }

    @Override
    public String getEndpoint() {
        return "analyze_issues";
    }

    @Override
    public void readResponse(String response) {
        if (response == null) {
            this.response = null;
            return;
        }
        String issuesStr = response.replace("'", "");
        if (issuesStr.trim().isBlank()) {
            this.response = null;
            return;
        }

        String[] splited = issuesStr.split(",");

        List<AnalyzeIssuesResponse.Issue> issuesFoundList = new ArrayList<>();
        Map<String, Issue> pastIssueMap = pastIssues.stream().collect(Collectors.toMap(t -> t.getId().toString(), Function.identity()));

        for (String foundIssue : splited) {
            Issue i = pastIssueMap.get(foundIssue.trim());
            if (i != null) {
                issuesFoundList.add(new AnalyzeIssuesResponse.Issue(foundIssue.trim(), i.getTitle(), i.getDescription()));
            }
        }

        if (issuesFoundList.isEmpty()) {
            this.response = null;
            return;
        }

        this.response = AnalyzeIssuesResponse.builder().issues(issuesFoundList).similar(true).build();
    }

    public AnalyzeIssuesResponse getResponse() {
        return response;
    }
}
