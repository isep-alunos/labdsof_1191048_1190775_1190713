package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.issue.IssueExtraInfo;
import isep.labdsof.backend.domain.models.issue.IssueLocation;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.domain.requests.AnalyseIssueRequest;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.ReactToIssueRequest;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.domain.responses.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.ai.ClarificationResponse;
import isep.labdsof.backend.repositories.IssueRepository;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import isep.labdsof.backend.services.ai.AiService;
import isep.labdsof.backend.services.ai.AnalyseIssuesAiRequest;
import isep.labdsof.backend.services.ai.ClarificationAiRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static isep.labdsof.backend.domain.exceptions.AppCustomExceptions.ISSUE_NOT_FOUND;
import static isep.labdsof.backend.domain.exceptions.AppCustomExceptions.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssueServiceImpl implements IssueService {

    private final EventService eventService;
    private final AiService aiService;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    private static final String ISSUE_CREATED = "Issue Created!";

    @Override
    public AnalyzeIssuesResponse analyseIssue(final String userEmail, final AnalyseIssueRequest analyseIssueRequest) throws LabdsofCustomException {

        final User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));

        Event event = eventService.getByName(analyseIssueRequest.eventName);
        IssueLocation location = new IssueLocation(analyseIssueRequest.location);

        Issue issue = new Issue(user, analyseIssueRequest.title, analyseIssueRequest.description, location, event);

        AnalyzeIssuesResponse response = validateRepeatedIssue(issue, event, new RestTemplate());

        if (response == null) {
            new AnalyzeIssuesResponse();
            response = AnalyzeIssuesResponse.builder().created(true).criticality(MessageCriticality.INFO).message("").build();

        } else if (!response.isSimilar()) {
            response.setCreated(true);
            response.setMessage("");
            response.setCriticality(MessageCriticality.INFO);
        }

        return response;

    }

    @Override
    public MessageDto createIssue(String userEmail, CreateIssueRequest createIssueRequest) throws LabdsofCustomException {
        final User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));

        Event event = eventService.getByName(createIssueRequest.eventName);
        IssueLocation location = new IssueLocation(createIssueRequest.location);

        Issue issue = new Issue(user, createIssueRequest.title, createIssueRequest.description, location, event);

        List<IssueExtraInfo> issueExtraInfos = new ArrayList<>();

        int i = 0;
        for (String question : createIssueRequest.getQuestions()) {
            IssueExtraInfo issueExtraInfo = new IssueExtraInfo(question, createIssueRequest.getExtraInfo().get(String.valueOf(i)));
            issueExtraInfos.add(issueExtraInfo);
            i++;
        }

        issue.setIssueExtraInfo(issueExtraInfos);
        issueRepository.save(issue);

        try {
            final UserProfile userProfile = userProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));
            userProfile.addPointsForReportIssue();
            userProfileRepository.save(userProfile);
        } catch (Exception e) {
            log.error("Error adding points to user", e);
        }

        return new MessageDto(ISSUE_CREATED, MessageCriticality.INFO);

    }

    @Override
    public ClarificationResponse getClarificationQuestions(final String userEmail, AnalyseIssueRequest analyseIssueRequest) throws LabdsofCustomException {
        final User user = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));

        Event event = eventService.getByName(analyseIssueRequest.eventName);
        IssueLocation location = new IssueLocation(analyseIssueRequest.location);

        Issue issue = new Issue(user, analyseIssueRequest.title, analyseIssueRequest.description, location, event);

        ClarificationAiRequest request = new ClarificationAiRequest(issue);
        aiService.callAi(request, new RestTemplate());
        return request.getResponse();
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

    @Override
    public IssueDto praiseToIssue(final ReactToIssueRequest request, final String email) throws LabdsofCustomException {
        final Issue issue = issueRepository.findById(UUID.fromString(request.getIssueId())).orElseThrow(() -> new LabdsofCustomException(ISSUE_NOT_FOUND));
        final User user = userRepository.findUserByEmail(email).orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));

        issue.addPraise(user);

        try {
            final UserProfile userReporterProfile = userProfileRepository.findByUserId(issue.getEventWorkerAssigned().getId()).orElseThrow(() -> new LabdsofCustomException(USER_NOT_FOUND));
            userReporterProfile.addPointsForPraise();
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

        AnalyseIssuesAiRequest request = new AnalyseIssuesAiRequest(issue, pastIssues);
        aiService.callAi(request, restTemplate);
        return request.getResponse();
    }
}
