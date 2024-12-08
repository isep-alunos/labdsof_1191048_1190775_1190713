package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.requests.UpdateIssueRequest;
import isep.labdsof.backend.domain.responses.EventWorkerIssuesResponse;
import isep.labdsof.backend.services.EventWorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event-worker")
@RequiredArgsConstructor
public class EventWorkerController {

    private final EventWorkerService eventWorkerService;


    @GetMapping("/issues")
    public ResponseEntity<List<EventWorkerIssuesResponse>> getIssues(@AuthenticationPrincipal OAuth2IntrospectionAuthenticatedPrincipal principal) {
        final List<EventWorkerIssuesResponse> response = eventWorkerService.getEventWorkerIssues(principal.getAttribute("email"));
        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/issues/update")
    public ResponseEntity<IssueDto> updateIssue(@AuthenticationPrincipal OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                @RequestBody UpdateIssueRequest request) throws LabdsofCustomException {
        final IssueDto response = eventWorkerService.updateIssue(principal.getAttribute("email"), request);
        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(response);
    }
}
