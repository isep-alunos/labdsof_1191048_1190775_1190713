package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.issue.IssueLocation;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.ReactToIssueRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.repositories.IssueRepository;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static isep.labdsof.backend.domain.exceptions.AppCustomExceptions.ISSUE_NOT_FOUND;
import static isep.labdsof.backend.domain.exceptions.AppCustomExceptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueServiceImpl implements IssueService {

    private final EventService eventService;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    private static final String ISSUE_CREATED = "Issue Created!";

    @Override
    public AnalyzeIssuesResponse create(final String userEmail, final CreateIssueRequest createIssueRequest) throws LabdsofCustomException {

        final User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));

        Event event = eventService.getByName(createIssueRequest.eventName);
        IssueLocation location = new IssueLocation(createIssueRequest.location);

        Issue issue = new Issue(user, createIssueRequest.title, createIssueRequest.description, location, event);

        AnalyzeIssuesResponse response = null;

        if (!createIssueRequest.force) {
            response = validateRepeatedIssue(issue, event, new RestTemplate());
        }

        if (response == null) {
            issueRepository.save(issue);
            new AnalyzeIssuesResponse();
            response = AnalyzeIssuesResponse.builder().created(true).criticality(MessageCriticality.INFO).message(ISSUE_CREATED).build();

        } else if (!response.isSimilar()) {
            issueRepository.save(issue);
            response.setCreated(true);
            response.setMessage(ISSUE_CREATED);
            response.setCriticality(MessageCriticality.INFO);
        }

        if (response.isCreated()) {
            try {
                final UserProfile userProfile = userProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));
                userProfile.addPointsForReportIssue();
                userProfileRepository.save(userProfile);
            } catch (Exception e) {
                log.error("Error adding points to user", e);
            }
        }

        return response;

    }

    @Override
    public List<IssueDto> getIssuesByEventName(final String userEmail, final String eventName) throws LabdsofCustomException {
        final Event event = eventService.getByName(eventName);
        final List<Issue> issues = issueRepository.getIssuesByEvent_Name(event.getName());

        return issues.stream().map(i -> i.toDto(userEmail)).toList();
    }

    @Override
    public IssueDto reactToIssue(final ReactToIssueRequest request, final String email) throws LabdsofCustomException {
        final Issue issue = issueRepository.findById(UUID.fromString(request.getIssueId())).orElseThrow(() -> new LabdsofCustomException(ISSUE_NOT_FOUND));
        final User user = userRepository.findUserByEmail(email).orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));

        issue.addReaction(user);

        try {
            final UserProfile userReporterProfile = userProfileRepository.findByUserId(issue.getUserReporter().getId()).orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));
            userReporterProfile.addPointsForReaction();
            userProfileRepository.save(userReporterProfile);
        } catch (Exception e) {
            log.error("Error adding points to user reporter", e);
        }

        issueRepository.save(issue);

        return issue.toDto(email);
    }

    // AI validation

    public AnalyzeIssuesResponse validateRepeatedIssue(Issue issue, Event event, RestTemplate restTemplate) {
        List<Issue> pastIssues = issueRepository.getIssueByEvent(event);
        if (pastIssues.isEmpty()) return null;

        String pythonApiUrl = "http://localhost:8081/analyze_issues";

        AnalyzeIssuesRequest requestPayload = new AnalyzeIssuesRequest();
        requestPayload.setCurrentIssue(issue.toMap());
        requestPayload.setPastIssues(pastIssues.stream().map(Issue::toMap).toList());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AnalyzeIssuesRequest> requestEntity = new HttpEntity<>(requestPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String issuesStr = response.getBody().replace("'", "");
                if (issuesStr.trim().isBlank()) {
                    return null;
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

                if (issuesFoundList.isEmpty()) return null;

                return AnalyzeIssuesResponse.builder().issues(issuesFoundList).similar(true).build();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }
}
