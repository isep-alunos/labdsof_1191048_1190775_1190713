package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/event-manager")
@RequiredArgsConstructor
public class EventManagerController {


    private final EventService eventService;

    @PostMapping("/event")
    public ResponseEntity<MessageDto> createEvent(@RequestBody final CreateEventRequest request) throws Exception {
        eventService.create(request);
        return ResponseEntity.status(201).body(new MessageDto("Event created successfully"));
    }

    @PostMapping("/event/image")
    public ResponseEntity<MessageDto> uploadEventMap(@RequestParam("eventName") final String eventName, @RequestParam("image") final MultipartFile multipartImage) throws Exception {
        final MessageDto message = eventService.uploadEventMap(eventName, multipartImage);
        return ResponseEntity.status(201).body(message);
    }


    @GetMapping("/get-event-workers")
    public ResponseEntity<List<EventWorkersDto>> getEventWorkers() throws Exception {
        return ResponseEntity.status(200).body(eventService.getEventWorkers());
    }


}
