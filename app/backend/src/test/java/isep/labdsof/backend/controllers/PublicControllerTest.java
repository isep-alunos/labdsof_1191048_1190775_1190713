
package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.services.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PublicControllerTest {

    private final EventService eventService = Mockito.mock(EventService.class);
    private final PublicController publicController = new PublicController(eventService);

    @Test
    public void testGetEvents() {
        final List<EventDto> events = Collections.singletonList(EventDto.builder().build());
        when(eventService.getEvents()).thenReturn(events);

        ResponseEntity<List<EventDto>> response = publicController.getEvents();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(events, response.getBody());
    }
}