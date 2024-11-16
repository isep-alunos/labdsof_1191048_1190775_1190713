package isep.labdsof.backend.services.implementations;


import isep.labdsof.backend.domain.exceptions.event.EventInvalidFieldException;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.repositories.EventRepository;
import isep.labdsof.backend.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class  EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public void create(CreateEventRequest createEventRequest) throws EventInvalidFieldException {

        Event e = new Event(
                createEventRequest.name,
                createEventRequest.description,
                createEventRequest.startDate,
                createEventRequest.endDate,
                createEventRequest.maxParticipants,
                createEventRequest.eventWebsite,
                null
        );

        eventRepository.save(e);
    }
}
