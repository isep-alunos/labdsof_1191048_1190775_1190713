package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.EntityNotFoundException;
import isep.labdsof.backend.domain.exceptions.EventInvalidFieldException;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.requests.CreateEventRequest;

import java.util.List;
import java.util.UUID;

public interface EventService {

    void create(CreateEventRequest createEventRequest) throws Exception;
    List<EventWorkersDto> getEventWorkers() throws Exception;
    List<EventDto> getEvents();
    boolean eventExists(UUID id);
    void addIssue(Issue issue, UUID eventId) throws EntityNotFoundException, EventInvalidFieldException;
}
