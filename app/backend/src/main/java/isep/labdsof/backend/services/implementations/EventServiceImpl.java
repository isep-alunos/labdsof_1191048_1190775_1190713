package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.EntityNotFoundException;
import isep.labdsof.backend.domain.exceptions.EventInvalidFieldException;
import isep.labdsof.backend.domain.models.event.Address;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.event.EventLocation;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.repositories.EventRepository;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    @Override
    public void create(CreateEventRequest createEventRequest) throws Exception {

        Address address = new Address(
                createEventRequest.street,
                createEventRequest.number,
                createEventRequest.postalCode
        );

        EventLocation location = new EventLocation(
                createEventRequest.latitude,
                createEventRequest.longitude,
                address
        );

        List<User> ewList = new ArrayList<>();

        for (UUID id : createEventRequest.eventWorkers) {
            ewList.add(userService.getById(id));
        }

        Event e = new Event(
                createEventRequest.name,
                createEventRequest.description,
                createEventRequest.startDate,
                createEventRequest.endDate,
                createEventRequest.maxParticipants,
                createEventRequest.eventWebsite,
                location,
                ewList
        );

        eventRepository.save(e);
    }

    @Override
    public List<EventWorkersDto> getEventWorkers() throws Exception {
        List<User> eventWorkers = userService.getEventWorkers();
        return eventWorkers.stream().map(e -> EventWorkersDto
                .builder()
                .name(e.getNome())
                .email(e.getEmail())
                .id(e.getId())
                .build()
        ).toList();
    }

    @Override
    public List<EventDto> getEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(Event::toDto).toList();
    }

    @Override
    public boolean eventExists(UUID id) {
        return false;
    }

    @Override
    public void addIssue(Issue issue, UUID eventId) throws EntityNotFoundException, EventInvalidFieldException {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (!eventOpt.isPresent()) {
            throw new EntityNotFoundException("Event not found");
        }

        Event event = eventOpt.get();
        event.newIssue(issue);
        eventRepository.save(event);
    }
}
