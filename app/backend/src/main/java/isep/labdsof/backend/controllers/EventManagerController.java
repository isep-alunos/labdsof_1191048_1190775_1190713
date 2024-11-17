package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event-manager")
@RequiredArgsConstructor
public class EventManagerController {


    private final EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<MessageDto> createEvent(@RequestBody CreateEventRequest request) throws Exception {
        eventService.create(request);
        return ResponseEntity.status(201).body(new MessageDto("Event created successfully"));
    }


}
