package isep.labdsof.backend.services.implementations;

import com.google.gson.Gson;
import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.models.BaseEntity;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.issue.IssueLocation;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.repositories.IssueRepository;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final EventService eventService;
    private final IssueRepository issueRepository;

    private static final String ISSUE_CREATED = "Issue Created!";

    @Override
    public AnalyzeIssuesResponse create(CreateIssueRequest createIssueRequest) throws Exception {

        Event event = eventService.getByName(createIssueRequest.eventName);
        IssueLocation location = new IssueLocation(createIssueRequest.location);

        Issue issue = new Issue(
                createIssueRequest.title,
                createIssueRequest.description,
                location,
                event
        );

        AnalyzeIssuesResponse response = null;

        if (!createIssueRequest.force) {
            response = validateRepeatedIssue(issue, event, new RestTemplate());
        }

        if (response == null) {
            issueRepository.save(issue);
            response = new AnalyzeIssuesResponse()
                    .builder()
                    .created(true)
                    .criticality(MessageCriticality.INFO)
                    .message(ISSUE_CREATED)
                    .build();

        } else if (!response.isSimilar()) {
            issueRepository.save(issue);
            response.setCreated(true);
            response.setMessage(ISSUE_CREATED);
            response.setCriticality(MessageCriticality.INFO);
        }

        return response;

    }

    @Override
    public List<IssueDto> getIssuesByEventName(String eventName) throws Exception {
        final Event event = eventService.getByName(eventName);
        final List<Issue> issues = issueRepository.getIssuesByEvent_Name(event.getName());

        return issues.stream().map(Issue::toDto).toList();
    }

    // AI validation

    public AnalyzeIssuesResponse validateRepeatedIssue(Issue issue, Event event, RestTemplate restTemplate) {
        List<Issue> pastIssues = issueRepository.getIssueByEvent(event);
        if (pastIssues.size() == 0) return null;

        String pythonApiUrl = "http://localhost:8081/analyze_issues";

        AnalyzeIssuesRequest requestPayload = new AnalyzeIssuesRequest();
        requestPayload.setCurrentIssue(issue.toMap());
        requestPayload.setPastIssues(pastIssues.stream().map(Issue::toMap).toList());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnalyzeIssuesRequest> requestEntity = new HttpEntity<>(requestPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    pythonApiUrl,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                String issuesStr = response.getBody().replace("'", "");
                if (issuesStr.trim().isBlank()) {
                    return null;
                }

                String[] splited = issuesStr.split(",");

                List<AnalyzeIssuesResponse.Issue> issuesFoundList = new ArrayList<>();
                Map<String, Issue> pastIssueMap = pastIssues.stream()
                        .collect(Collectors.toMap(
                                t -> t.getId().toString(),
                                Function.identity()
                        ));

                for (String foundIssue : splited) {
                    Issue i = pastIssueMap.get(foundIssue.trim());
                    if (i != null) {
                        issuesFoundList
                                .add(new AnalyzeIssuesResponse.Issue(foundIssue.trim(),
                                        i.getTitle(),
                                        i.getDescription()));
                    }
                }

                if (issuesFoundList.isEmpty()) return null;
                AnalyzeIssuesResponse analyzeIssuesResponse = AnalyzeIssuesResponse
                        .builder()
                        .issues(issuesFoundList)
                        .similar(true)
                        .build();


                return analyzeIssuesResponse;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }
}
