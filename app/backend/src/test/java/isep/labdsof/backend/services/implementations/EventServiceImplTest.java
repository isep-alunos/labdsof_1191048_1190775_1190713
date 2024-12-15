
package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.EventWorkersDto;
import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.event.Address;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.event.EventLocation;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.userProfile.UserProfile;
import isep.labdsof.backend.domain.requests.CreateEventRequest;
import isep.labdsof.backend.domain.requests.MarkPresenceAtEventRequest;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.repositories.EventRepository;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.services.UserProfileService;
import isep.labdsof.backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserProfileService userProfileService;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void testCreateEvent() throws LabdsofCustomException {
        final UUID userUuid = UUID.randomUUID();
        final CreateEventRequest request = CreateEventRequest.builder()
                .name("Test Event")
                .description("Test Description")
                .street("Test Street")
                .number(123)
                .postalCode("4100-000")
                .latitude(0.0)
                .longitude(0.0)
                .eventWorkers(List.of(userUuid))
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .maxParticipants(10)
                .eventWebsite("http://test.com")
                .build();

        final User user = new User(userUuid, "email@email.com", "Test User", List.of(Role.USER, Role.EVENT_WORKER));
        when(userService.getById(any(UUID.class))).thenReturn(user);

        eventService.create(request);

        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testGetEventWorkers() {
        final User user = new User(UUID.randomUUID(), "test@example.com", "Test User", List.of(Role.USER));
        when(userService.getEventWorkers()).thenReturn(List.of(user));

        final List<EventWorkersDto> result = eventService.getEventWorkers();

        assertEquals(1, result.size());
        assertEquals("Test User", result.getFirst().getName());
        assertEquals("test@example.com", result.getFirst().getEmail());
        verify(userService).getEventWorkers();
    }

    @Test
    void testMarkPresenceAtEvent() throws Exception {
        final MarkPresenceAtEventRequest request = new MarkPresenceAtEventRequest();
        request.setName("Test Event");
        request.setLatitude(0.0);
        request.setLongitude(0.0);

        final UserProfile userProfile = new UserProfile();
        userProfile.setAttendedEvents(new ArrayList<>());
        final Event event = new Event();
        event.setLocation(new EventLocation(0.0, 0.0, new Address("Test Street", 123, "4100-000")));
        event.setName("Test Event");

        when(userProfileService.getByUserEmail(anyString())).thenReturn(userProfile);
        when(eventRepository.findByName(anyString())).thenReturn(Optional.of(event));

        final StatusResponse response = eventService.markPresenceAtEvent(request, "test@example.com");

        assertTrue(response.isSuccess());
        verify(userProfileService).getByUserEmail(anyString());
        verify(eventRepository).findByName(anyString());
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void testGetEvents() throws LabdsofCustomException {
        final Event event = new Event("Test Event", "Test Description", LocalDateTime.now(),
                LocalDateTime.now().plusDays(2), 10, "http://test.com",
                new EventLocation(0.0, 0.0, new Address("Test Street", 123, "4100-000")),
                List.of(new User(UUID.randomUUID(), "email@email.com", "Test User",
                        List.of(Role.USER, Role.EVENT_WORKER))), null);
        when(eventRepository.findAll()).thenReturn(List.of(event));

        final List<EventDto> result = eventService.getEvents();

        assertEquals(1, result.size());
        verify(eventRepository).findAll();
    }

    @Test
    void testGetEventById() throws LabdsofCustomException {
        final UUID eventId = UUID.randomUUID();
        final Event event = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        final Event result = eventService.getEvent(eventId);

        assertEquals(event, result);
        verify(eventRepository).findById(eventId);
    }

    @Test
    void testGetEventById_NotFound() {
        final UUID eventId = UUID.randomUUID();
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(LabdsofCustomException.class, () -> eventService.getEvent(eventId));
        verify(eventRepository).findById(eventId);
    }

    @Test
    void testGetEventByName() throws LabdsofCustomException {
        final String eventName = "Test Event";
        final Event event = new Event();
        when(eventRepository.findByName(eventName)).thenReturn(Optional.of(event));

        final Event result = eventService.getByName(eventName);

        assertEquals(event, result);
        verify(eventRepository).findByName(eventName);
    }

    @Test
    void testGetEventByName_NotFound() {
        final String eventName = "Test Event";
        when(eventRepository.findByName(eventName)).thenReturn(Optional.empty());

        assertThrows(LabdsofCustomException.class, () -> eventService.getByName(eventName));
        verify(eventRepository).findByName(eventName);
    }
}