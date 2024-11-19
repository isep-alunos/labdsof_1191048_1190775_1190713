package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/event-manager")
@RequiredArgsConstructor
public class EventManagerController {


    private final EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<MessageDto> createEvent(@RequestBody CreateEventRequest request) throws Exception {
        eventService.create(request);
        return ResponseEntity.status(201).body(new MessageDto("Event created successfully"));
    }


    @GetMapping("/get-event-workers")
    public ResponseEntity<List<EventWorkersDto>> getEventWorkers() throws Exception {
        return ResponseEntity.status(200).body(eventService.getEventWorkers());
    }


}
