package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.exceptions.event.EventInvalidFieldException;
import isep.labdsof.backend.domain.requests.CreateEventRequest;

public interface EventService {

    void create(CreateEventRequest createEventRequest) throws EventInvalidFieldException;
}
