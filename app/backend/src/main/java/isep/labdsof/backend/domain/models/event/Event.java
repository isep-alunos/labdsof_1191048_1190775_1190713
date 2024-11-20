package isep.labdsof.backend.domain.models.event;

import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.EventInvalidFieldException;
import isep.labdsof.backend.domain.models.BaseEntity;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@NoArgsConstructor
public class Event extends BaseEntity {

    @Column(unique = true)
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipants;
    private String eventWebsite;
    private EventLocation location;
    @ManyToMany
    private List<User> eventWorkers;

    private static final String URL_REGEX = "^(https?://)?" +   // Optional HTTP or HTTPS
            "([\\w-]+\\.)+[\\w-]+" +                            // Domain name
            "(:\\d+)?(/.*)?$";                                  // Optional port and path

    public Event(String name, String description, LocalDateTime startDate, LocalDateTime endDate, Integer maxParticipants, String eventWebsite, EventLocation location, List<User> ewList) throws EventInvalidFieldException {
        setName(name);
        setDescription(description);
        setDateRange(startDate, endDate);
        setMaxParticipants(maxParticipants);
        setEventWebsite(eventWebsite);
        setLocation(location);
        setEventWorkerList(ewList);
    }



    public void setName(String name) throws EventInvalidFieldException {
        if (name == null || name.isBlank()) {
            throw new EventInvalidFieldException("Empty event name");
        } else if (name.trim().length() > 20) {
            throw new EventInvalidFieldException("Event name should not" +
                    " be greater than 20 characters");
        }

        this.name = name.trim();
    }

    public void setDescription(String description) throws EventInvalidFieldException {
        if (description == null || description.isBlank()) {
            throw new EventInvalidFieldException("Empty Event Description");
        } else if (description.trim().length() > 500) {
            throw new EventInvalidFieldException("Event description should not" + " be greater than 500 characters");
        }

        this.description = description.trim();
    }

    public void setDateRange(LocalDateTime startDate, LocalDateTime endDate) throws EventInvalidFieldException {
        if (startDate == null) {
            throw new EventInvalidFieldException("Empty Start Date");
        } else if (endDate == null) {
            throw new EventInvalidFieldException("Empty End Date");
        } else if (startDate.isAfter(endDate)) {
            throw new EventInvalidFieldException("The event start date should " + "not be after the end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public void setMaxParticipants(Integer maxParticipants) throws EventInvalidFieldException {
        if (maxParticipants == null) {
            throw new EventInvalidFieldException("Empty max participants");
        } else if (maxParticipants < 1) {
            throw new EventInvalidFieldException("The event number of participants" + " should be greater or equal than 1");
        }
        this.maxParticipants = maxParticipants;
    }

    public void setEventWebsite(String url) throws EventInvalidFieldException {
        Pattern pattern = Pattern.compile(URL_REGEX);
        if (url == null || url.isBlank()) {
            throw new EventInvalidFieldException("The event website should not be empty");
        } else if (!pattern.matcher(url.trim()).matches()) {
            throw new EventInvalidFieldException("Invalid event website");
        }

        this.eventWebsite = url.trim();
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    private void setEventWorkerList(List<User> ewList) throws EventInvalidFieldException {
        for (User u : ewList) {
            if (!u.hasRole(Role.EVENT_WORKER)) {
                throw new EventInvalidFieldException("Event workers should have EVENT_WORKER role");
            }
        }
        this.eventWorkers = ewList;
    }

    public EventDto toDto() {
        return EventDto.builder()
                .name(name)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .maxParticipants(maxParticipants)
                .eventWebsite(eventWebsite)
                .location(location.toDto())
                .eventWorkerNames(eventWorkers.stream().map(User::getNome).toList())
                .build();
    }
}
