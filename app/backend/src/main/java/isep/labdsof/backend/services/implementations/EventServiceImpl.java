package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.exceptions.EventNotFoundException;
import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.EntityNotFoundException;
import isep.labdsof.backend.domain.exceptions.EventInvalidFieldException;
import isep.labdsof.backend.domain.exceptions.MarkPresenceNotNearEventException;
import isep.labdsof.backend.domain.models.event.Address;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.event.EventLocation;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.repositories.EventRepository;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.UserProfileService;
import isep.labdsof.backend.services.UserService;
import jakarta.transaction.Transactional;
import isep.labdsof.backend.utils.LocationUtils;
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
    private final UserProfileService userProfileService;
    private final UserProfileRepository userProfileRepository;

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
    public Event getByName(String name) throws EventNotFoundException {
        final Optional<Event> eventOpt = eventRepository.findByName(name);
        if (eventOpt.isEmpty()) {
            throw new EventNotFoundException("Event with name " + name + " not found");
        }
        return eventOpt.get();
    }

    @Override
    public StatusResponse markPresenceAtEvent(MarkPresenceAtEventRequest request, String userEmail) {
        final double minDistanceRequiredInMeters = 100.0;
        try {
            final UserProfile userProfile = userProfileService.getByUserEmail(userEmail);
            final Event event = getByName(request.name);
            final double distanceFromEventInMeters = LocationUtils.calculateDistance(request.getLatitude(), request.getLongitude(), event.getLocation().getLatitude(), event.getLocation().getLongitude());
            if(distanceFromEventInMeters >= minDistanceRequiredInMeters){
                throw new MarkPresenceNotNearEventException(String.format("You must be within %.2f meters to mark presence. You are only %.2f meters away.", minDistanceRequiredInMeters, distanceFromEventInMeters));
            }
            final boolean success = userProfile.addAttendedEvent(event);
            userProfileRepository.save(userProfile);
            return StatusResponse.builder()
                    .success(success)
                    .build();
        }catch (Exception ex){
            return StatusResponse.builder()
                    .success(false)
                    .messages(List.of(new MessageDto(ex.getMessage(), MessageCriticality.ERROR)))
                    .build();
        }
    }

    @Override
    public List<EventDto> getEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(Event::toDto).toList();
    }

    @Override
    public Event getEvent(UUID id) throws EntityNotFoundException {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (!eventOpt.isPresent()) {
            throw new EntityNotFoundException("Event not found");
        }
        return eventOpt.get();
    }
}
