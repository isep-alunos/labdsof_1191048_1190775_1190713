package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.exceptions.EntityNotFoundException;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.issue.IssueLocation;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.repositories.IssueRepository;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private EventService eventService;
    private IssueRepository issueRepository;

    @Override
    public void create(CreateIssueRequest createIssueRequest) throws Exception {

        boolean eventExists = eventService.eventExists(createIssueRequest.eventId);
        if (!eventExists) {
            throw new EntityNotFoundException("Event not found");
        }

        IssueLocation location = new IssueLocation(createIssueRequest.location);

        Issue issue = new Issue(
                createIssueRequest.title,
                createIssueRequest.description,
                location
        );

        // TODO VALIDATE IF ISUE IS ALREADY REPORTED USING AI

        issueRepository.save(issue);

        eventService.addIssue(issue, createIssueRequest.eventId);
    }
}
