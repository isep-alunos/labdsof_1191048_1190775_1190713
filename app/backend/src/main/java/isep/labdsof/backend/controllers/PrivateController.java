package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.dtos.user.UserProfileDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.requests.ReactToIssueRequest;
import isep.labdsof.backend.domain.requests.ai.AnalyzeIssuesResponse;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.IssueService;
import isep.labdsof.backend.services.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private")
public class PrivateController {

    private final EventService eventService;
    private final IssueService issueService;
    private final UserProfileService userProfileService;

    @PutMapping("/markPresence")
    public ResponseEntity<StatusResponse> markPresenceAtEvent(@AuthenticationPrincipal final OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                              @Valid @RequestBody final MarkPresenceAtEventRequest request) throws LabdsofCustomException {
        final StatusResponse response = eventService.markPresenceAtEvent(request, principal.getAttribute("email"));
        if (response.isSuccess())
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @PostMapping("/create-issue")
    public ResponseEntity<AnalyzeIssuesResponse> createIssue(@AuthenticationPrincipal final OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                             @RequestBody final CreateIssueRequest request) throws LabdsofCustomException {
        AnalyzeIssuesResponse response = issueService.create(principal.getAttribute("email"), request);
        if (response.isCreated()) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/eventIssues/{eventName}")
    public ResponseEntity<List<IssueDto>> getIssuesByEvent(@AuthenticationPrincipal final OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                           @PathVariable final String eventName) throws LabdsofCustomException {
        final List<IssueDto> result = issueService.getIssuesByEventName(principal.getAttribute("email"), eventName);

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal final OAuth2IntrospectionAuthenticatedPrincipal principal) throws LabdsofCustomException {
        final UserProfileDto userProfile = userProfileService.getByUserEmail(principal.getAttribute("email")).toDto();
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/eventIssues/react")
    public ResponseEntity<IssueDto> reactToIssue(@AuthenticationPrincipal final OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                 @Valid @RequestBody final ReactToIssueRequest request) throws LabdsofCustomException {
        final IssueDto response = issueService.reactToIssue(request, principal.getAttribute("email"));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/eventIssues/praise")
    public ResponseEntity<IssueDto> praiseToIssue(@AuthenticationPrincipal final OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                  @Valid @RequestBody final ReactToIssueRequest request) throws LabdsofCustomException {
        final IssueDto response = issueService.praiseToIssue(request, principal.getAttribute("email"));
        return ResponseEntity.ok(response);
    }
}