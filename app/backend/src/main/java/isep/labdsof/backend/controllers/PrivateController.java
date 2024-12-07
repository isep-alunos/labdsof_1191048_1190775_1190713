package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import isep.labdsof.backend.domain.dtos.user.UserProfileDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.requests.CreateIssueRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
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
    public ResponseEntity<StatusResponse> markPresenceAtEvent(@AuthenticationPrincipal OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                              @Valid @RequestBody MarkPresenceAtEventRequest request) throws LabdsofCustomException {
        final StatusResponse response = eventService.markPresenceAtEvent(request, principal.getAttribute("email"));
        if (response.isSuccess())
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @PostMapping("/create-issue")
    public ResponseEntity<AnalyzeIssuesResponse> createIssue(@RequestBody CreateIssueRequest request) throws LabdsofCustomException {
        AnalyzeIssuesResponse response = issueService.create(request);
        if (response.isCreated()) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.status(200).body(response);
        }
    }

    @GetMapping("/eventIssues/{eventName}")
    public ResponseEntity<List<IssueDto>> getIssuesByEvent(@PathVariable String eventName) throws LabdsofCustomException {
        final List<IssueDto> result = issueService.getIssuesByEventName(eventName);

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal OAuth2IntrospectionAuthenticatedPrincipal principal) throws LabdsofCustomException {
        final UserProfileDto userProfile = userProfileService.getByUserEmail(principal.getAttribute("email")).toDto();
        return ResponseEntity.ok(userProfile);
    }


}
