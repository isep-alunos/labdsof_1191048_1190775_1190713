package isep.labdsof.backend.services.implementations;

import com.google.gson.Gson;
import isep.labdsof.backend.domain.dtos.issue.IssueDto;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final EventService eventService;
    private final IssueRepository issueRepository;

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
            response = validateRepeatedIssue(issue, event);
        }

        if (response == null) {
            issueRepository.save(issue);
            response = new AnalyzeIssuesResponse()
                    .builder()
                    .created(true)
                    .criticality(MessageCriticality.INFO)
                    .message("Issue Created!")
                    .build();

        } else if (!response.isSimilar()) {
            issueRepository.save(issue);
            response.setCreated(true);
            response.setMessage("Issue Created!");
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

    private AnalyzeIssuesResponse validateRepeatedIssue(Issue issue, Event event) {
        List<Issue> pastIssues = issueRepository.getIssueByEvent(event);
        if (pastIssues.size() == 0) return null;

        RestTemplate restTemplate = new RestTemplate();
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
                String json = response.getBody();
                Gson gson = new Gson();

                // Parse the JSON string into a YourObject instance
                AnalyzeIssuesResponse parsedObject = gson.fromJson(json.substring(1, json.length() - 2).replace("\\", ""), AnalyzeIssuesResponse.class);

                return parsedObject;
                //return Arrays.asList(Objects.requireNonNull(response.getBody()));
            } else {
                throw new RuntimeException("Failed to call AI API");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with AI API: " + e.getMessage(), e);
        }

    }
}
