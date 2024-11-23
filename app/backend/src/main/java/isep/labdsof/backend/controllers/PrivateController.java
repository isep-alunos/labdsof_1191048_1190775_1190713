package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.requests.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private")
public class PrivateController {

    private final EventService eventService;
    private final IssueService issueService;

    @PutMapping("/markPresence")
    public ResponseEntity<StatusResponse> markPresenceAtEvent(@AuthenticationPrincipal OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                          @Valid @RequestBody MarkPresenceAtEventRequest request) throws Exception {
        final StatusResponse response = eventService.markPresenceAtEvent(request, principal.getAttribute("email"));
        if(response.isSuccess())
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("/create-issue")
    public ResponseEntity<AnalyzeIssuesResponse> createIssue(@RequestBody CreateIssueRequest request) throws Exception {
        AnalyzeIssuesResponse response = issueService.create(request);
        return ResponseEntity.status(201).body(response);
    }

}
