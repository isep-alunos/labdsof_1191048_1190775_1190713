package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.responses.StatusResponse;

import java.util.List;
import java.util.UUID;

public interface EventService {

    void create(CreateEventRequest createEventRequest) throws LabdsofCustomException;

    List<EventWorkersDto> getEventWorkers() throws LabdsofCustomException;

    StatusResponse markPresenceAtEvent(MarkPresenceAtEventRequest request, String userEmail) throws LabdsofCustomException;

    Event getByName(String name) throws LabdsofCustomException;

    List<EventDto> getEvents();

    Event getEvent(UUID id) throws LabdsofCustomException;
}
