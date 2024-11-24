
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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


    @Mock
    private RestTemplate restTemplate;


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

    @Test
    void testValidateRepeatedIssue_NoPastIssues() {
        Event mockEvent = new Event();
        Issue mockIssue = new Issue();

        when(issueRepository.getIssueByEvent(mockEvent)).thenReturn(new ArrayList<>());

        AnalyzeIssuesResponse response = issueService.validateRepeatedIssue(mockIssue, mockEvent, restTemplate);

        assertNull(response);
        verify(issueRepository, times(1)).getIssueByEvent(mockEvent);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testValidateRepeatedIssue_NoSimilarIssues() {
        Event mockEvent = new Event();
        Issue mockIssue = new Issue();

        List<Issue> pastIssues = List.of(new Issue());
        when(issueRepository.getIssueByEvent(mockEvent)).thenReturn(pastIssues);

        ResponseEntity<String> mockResponse = new ResponseEntity<>("", HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        AnalyzeIssuesResponse response = issueService.validateRepeatedIssue(mockIssue, mockEvent, restTemplate);

        assertNull(response);
        verify(issueRepository, times(1)).getIssueByEvent(mockEvent);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testValidateRepeatedIssue_SimilarIssuesFound() throws Exception {
        Event mockEvent = new Event();
        Issue mockIssue = new Issue();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        mockIssue.setId(id1);
        mockIssue.setTitle("Current Issue");
        mockIssue.setDescription("Current issue description");

        Issue pastIssue1 = new Issue();
        pastIssue1.setId(id2);
        pastIssue1.setTitle("Past Issue 1");
        pastIssue1.setDescription("Description of past issue 1");

        List<Issue> pastIssues = List.of(pastIssue1);
        when(issueRepository.getIssueByEvent(mockEvent)).thenReturn(pastIssues);

        String mockResponseBody = id2.toString(); // ID of the similar issue returned by the Python service
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        AnalyzeIssuesResponse response = issueService.validateRepeatedIssue(mockIssue, mockEvent, restTemplate);

        assertNotNull(response);
        assertTrue(response.isSimilar());
        assertEquals(1, response.getIssues().size());
        assertEquals("Past Issue 1", response.getIssues().get(0).getTitle());
        assertEquals("Description of past issue 1", response.getIssues().get(0).getDescription());

        verify(issueRepository, times(1)).getIssueByEvent(mockEvent);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testValidateRepeatedIssue_PythonServiceError() {
        Event mockEvent = new Event();
        Issue mockIssue = new Issue();

        List<Issue> pastIssues = List.of(new Issue());
        when(issueRepository.getIssueByEvent(mockEvent)).thenReturn(pastIssues);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Python service unavailable"));

        AnalyzeIssuesResponse response = issueService.validateRepeatedIssue(mockIssue, mockEvent, restTemplate);

        assertNull(response);
        verify(issueRepository, times(1)).getIssueByEvent(mockEvent);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }
}