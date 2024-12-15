package isep.labdsof.backend.controllers;


import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.services.EventService;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEvents() {
        final List<EventDto> result = eventService.getEvents();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/events/{eventName}")
    public ResponseEntity<EventDto> getEvent(@PathVariable final String eventName) throws LabdsofCustomException {
        final EventDto result = eventService.getEventByName(eventName);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/events/{eventName}/map", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getEventMap(@PathVariable final String eventName) throws LabdsofCustomException {
        final byte[] result = eventService.getEventMap(eventName);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ByteArrayResource(result));
    }
}
