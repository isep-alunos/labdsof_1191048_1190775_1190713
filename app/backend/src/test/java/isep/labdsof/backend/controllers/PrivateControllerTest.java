
package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PrivateControllerTest {

    private final EventService eventService = Mockito.mock(EventService.class);
    private final IssueService issueService = Mockito.mock(IssueService.class);
    private final PrivateController privateController = new PrivateController(eventService, issueService);

    @Test
    public void testMarkPresenceAtEvent() throws Exception {
        final MarkPresenceAtEventRequest request = new MarkPresenceAtEventRequest();
        final OAuth2IntrospectionAuthenticatedPrincipal principal = Mockito.mock(OAuth2IntrospectionAuthenticatedPrincipal.class);
        when(principal.getAttribute("email")).thenReturn("test@example.com");

        final StatusResponse statusResponse = new StatusResponse(true, Collections.emptyList());
        when(eventService.markPresenceAtEvent(request, "test@example.com")).thenReturn(statusResponse);

        ResponseEntity<StatusResponse> response = privateController.markPresenceAtEvent(principal, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(statusResponse, response.getBody());
    }

    @Test
    public void testCreateIssue() throws Exception {
        final CreateIssueRequest request = new CreateIssueRequest();
        final AnalyzeIssuesResponse analyzeIssuesResponse = AnalyzeIssuesResponse.builder().created(true).build();
        when(issueService.create(request)).thenReturn(analyzeIssuesResponse);

        ResponseEntity<AnalyzeIssuesResponse> response = privateController.createIssue(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(analyzeIssuesResponse, response.getBody());
    }

    @Test
    public void testGetIssuesByEvent() throws Exception {
        final List<IssueDto> issues = Collections.singletonList(IssueDto.builder().build());
        when(issueService.getIssuesByEventName("eventName")).thenReturn(issues);

        ResponseEntity<List<IssueDto>> response = privateController.getIssuesByEvent("eventName");

        assertEquals(201, response.getStatusCode().value());
        assertEquals(issues, response.getBody());
    }
}