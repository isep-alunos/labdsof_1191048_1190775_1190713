package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private")
public class PrivateController {

    private final EventService eventService;

    @PutMapping("/markPresence")
    public ResponseEntity<StatusResponse> markPresenceAtEvent(@AuthenticationPrincipal OAuth2IntrospectionAuthenticatedPrincipal principal,
                                                          @Valid @RequestBody MarkPresenceAtEventRequest request) throws Exception {
        final StatusResponse response = eventService.markPresenceAtEvent(request, principal.getAttribute("email"));
        return ResponseEntity.ok(response);
    }

}
