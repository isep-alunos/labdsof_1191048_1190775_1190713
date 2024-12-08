
package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import isep.labdsof.backend.services.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PrivateControllerTest {

    private final EventService eventService = Mockito.mock(EventService.class);
    private final IssueService issueService = Mockito.mock(IssueService.class);
    private final UserProfileService userProfileService = Mockito.mock(UserProfileService.class);
    private final PrivateController privateController = new PrivateController(eventService, issueService, userProfileService);

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
        when(issueService.create("userEmail", request)).thenReturn(analyzeIssuesResponse);
        final var principal = new OAuth2IntrospectionAuthenticatedPrincipal("user", Map.of("email", "userEmail"), Collections.emptyList());

        ResponseEntity<AnalyzeIssuesResponse> response = privateController.createIssue(principal, request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(analyzeIssuesResponse, response.getBody());
    }

    @Test
    public void testGetIssuesByEvent() throws Exception {
        final List<IssueDto> issues = Collections.singletonList(IssueDto.builder().build());
        when(issueService.getIssuesByEventName("userEmail", "eventName")).thenReturn(issues);

        final var principal = new OAuth2IntrospectionAuthenticatedPrincipal("user", Map.of("email", "userEmail"), Collections.emptyList());

        ResponseEntity<List<IssueDto>> response = privateController.getIssuesByEvent(principal, "eventName");

        assertEquals(201, response.getStatusCode().value());
        assertEquals(issues, response.getBody());
    }
}