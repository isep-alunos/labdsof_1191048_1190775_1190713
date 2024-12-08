package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.issue.IssueStatus;
import isep.labdsof.backend.domain.models.issue.IssueStatusUpdate;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.domain.requests.UpdateIssueRequest;
import isep.labdsof.backend.domain.responses.EventWorkerIssuesResponse;
import isep.labdsof.backend.repositories.EventRepository;
import isep.labdsof.backend.repositories.IssueRepository;
import isep.labdsof.backend.repositories.IssueStatusUpdateRepository;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.EventWorkerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class EventWorkerServiceImpl implements EventWorkerService {

    private final EventRepository eventRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final IssueStatusUpdateRepository issueStatusUpdateRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public List<EventWorkerIssuesResponse> getEventWorkerIssues(final String email) {
        final List<Event> eventsWorkerIsPartOf = eventRepository.findAllByWorkerEmail(email);
        final List<EventWorkerIssuesResponse> eventWorkerIssuesResponses = new ArrayList<>(eventsWorkerIsPartOf
                .parallelStream().map(e -> getEventWorkerIssuesResponse(email, e)).toList());
        eventWorkerIssuesResponses.sort(Comparator.comparing(e -> e.getEvent().getStartDate()));
        return eventWorkerIssuesResponses;

    }

    @Override
    public IssueDto updateIssue(final String email, final UpdateIssueRequest request) throws LabdsofCustomException {
        final LocalDateTime now = LocalDateTime.now();

        final User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new LabdsofCustomException(AppCustomExceptions.USER_NOT_FOUND));

        final Issue issue = issueRepository.findById(UUID.fromString(request.getIssueId())).orElseThrow(
                () -> new LabdsofCustomException(AppCustomExceptions.ISSUE_NOT_FOUND));

        if (request.isAssigned() && (issue.getEventWorkerAssigned() == null)) {
            issue.setEventWorkerAssigned(user);
        } else if (!request.isAssigned() && (issue.getEventWorkerAssigned() != null && issue.getEventWorkerAssigned().getEmail().equals(email))) {
            issue.setEventWorkerAssigned(null);
            issue.addIssueStatusUpdate(new IssueStatusUpdate(now, "Unsigned by user itself", issue.getIssueStatusUpdateList().getLast().getStatus()));
        }

        if (request.isAssigned() && issue.getEventWorkerAssigned() != null && issue.getEventWorkerAssigned().getEmail().equals(email)) {
            IssueStatusUpdate issueStatusUpdate = new IssueStatusUpdate(now, request.getDescription(), request.getStatus());
            issueStatusUpdate = issueStatusUpdateRepository.save(issueStatusUpdate);
            issue.addIssueStatusUpdate(issueStatusUpdate);
            addPointsIfIssueIsResolved(issue);
        }

        final Issue updatedIssue = issueRepository.save(issue);

        return updatedIssue.toDto(email);
    }

    private void addPointsIfIssueIsResolved(final Issue issue) {
        if (issue.getIssueStatusUpdateList().getLast().getStatus() == IssueStatus.RESOLVED) {
            final Optional<UserProfile> userProfile = userProfileRepository.findByUserId(issue.getUserReporter().getId());
            if (userProfile.isEmpty()) {
                log.error("User profile not found for user: {}. This user should get 3 points", issue.getUserReporter().getEmail());
                return;
            }
            final UserProfile profile = userProfile.get();
            profile.addPointsForIssueResolved();
            userProfileRepository.save(profile);
        }
    }

    private EventWorkerIssuesResponse getEventWorkerIssuesResponse(final String email, final Event event) {
        final List<Issue> issues = issueRepository.getIssueByEvent(event);
        return EventWorkerIssuesResponse.builder()
                .event(event.toDto())
                .issues(issues.stream().map(i -> i.toDto(email)).toList())
                .build();
    }
}

