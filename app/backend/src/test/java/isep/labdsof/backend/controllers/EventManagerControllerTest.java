
package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.services.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EventManagerControllerTest {

    private final EventService eventService = Mockito.mock(EventService.class);
    private final EventManagerController eventManagerController = new EventManagerController(eventService);

    @Test
    public void testCreateEvent() throws Exception {
        final CreateEventRequest request = CreateEventRequest.builder().build();
        final MessageDto messageDto = new MessageDto("Event created successfully");

        ResponseEntity<MessageDto> response = eventManagerController.createEvent(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(messageDto, response.getBody());
    }

    @Test
    public void testGetEventWorkers() throws Exception {
        final List<EventWorkersDto> eventWorkers = Collections.singletonList(new EventWorkersDto("name", "email@email", UUID.randomUUID()));
        when(eventService.getEventWorkers()).thenReturn(eventWorkers);

        ResponseEntity<List<EventWorkersDto>> response = eventManagerController.getEventWorkers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(eventWorkers, response.getBody());
    }
}