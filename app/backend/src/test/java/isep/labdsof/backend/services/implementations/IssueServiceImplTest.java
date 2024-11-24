
package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.models.event.Address;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.event.EventLocation;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.issue.IssueLocation;
import isep.labdsof.backend.domain.models.issue.IssueStatusUpdate;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.repositories.IssueRepository;
import isep.labdsof.backend.services.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceImplTest {

    @Mock
    private EventService eventService;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueServiceImpl issueService;

    @Test
    void testCreateIssue() throws Exception {
        final CreateIssueRequest request = new CreateIssueRequest();
        request.eventName = "Test Event";
        request.title = "Test Issue";
        request.description = "Test Description";
        request.location = "Test Location";
        request.force = true;

        final Event event = new Event();
        when(eventService.getByName(request.eventName)).thenReturn(event);

        final AnalyzeIssuesResponse response = issueService.create(request);

        assertTrue(response.isCreated());
        assertEquals("Issue Created!", response.getMessage());
        verify(issueRepository).save(any(Issue.class));
    }

    @Test
    void testGetIssuesByEventName() throws Exception {
        final String eventName = "Test Event";
        final Event event = new Event();
        event.setName(eventName);
        event.setLocation(new EventLocation(0.0,0.0, new Address()));
        final Issue issue = new Issue();
        issue.newStatusUpdate(new IssueStatusUpdate());
        issue.setEvent(event);
        issue.setLocation(new IssueLocation("Test Location"));
        when(eventService.getByName(eq(eventName))).thenReturn(event);
        when(issueRepository.getIssuesByEvent_Name(eq(eventName))).thenReturn(List.of(issue));

        final List<IssueDto> result = issueService.getIssuesByEventName(eventName);

        assertEquals(1, result.size());
        verify(eventService).getByName(eventName);
        verify(issueRepository).getIssuesByEvent_Name(eventName);
    }
}