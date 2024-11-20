package isep.labdsof.backend.controllers;


import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.services.EventService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getEvents(){
        final List<EventDto> result = eventService.getEvents();
        return ResponseEntity.ok(result);
    }
}
