package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.requests.CreateEventRequest;

import java.util.List;

public interface EventService {

    void create(CreateEventRequest createEventRequest) throws Exception;
    List<EventWorkersDto> getEventWorkers() throws Exception;
}
