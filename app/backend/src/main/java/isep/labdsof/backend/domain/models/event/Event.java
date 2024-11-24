package isep.labdsof.backend.domain.models.event;

import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.BaseEntity;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Getter
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

    private static final String URL_REGEX = "^(https?://)?" + // Optional HTTP or HTTPS
            "([\\w-]+\\.)+[\\w-]+" + // Domain name
            "(:\\d+)?(/.*)?$"; // Optional port and path

    public Event(final String name, final String description, final LocalDateTime startDate,
            final LocalDateTime endDate, final Integer maxParticipants, final String eventWebsite,
            final EventLocation location, final List<User> ewList) throws LabdsofCustomException {
        setName(name);
        setDescription(description);
        setDateRange(startDate, endDate);
        setMaxParticipants(maxParticipants);
        setEventWebsite(eventWebsite);
        setLocation(location);
        setEventWorkerList(ewList);
    }

    public void setName(String name) throws LabdsofCustomException {
        if (name == null || name.isBlank()) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty event name");
        } else if (name.trim().length() > 20) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Event name should not" +
                    " be greater than 20 characters");
        }

        this.name = name.trim();
    }

    public void setDescription(String description) throws LabdsofCustomException {
        if (description == null || description.isBlank()) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty Event Description");
        } else if (description.trim().length() > 500) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD,
                    "Event description should not" + " be greater than 500 characters");
        }

        this.description = description.trim();
    }

    public void setDateRange(LocalDateTime startDate, LocalDateTime endDate) throws LabdsofCustomException {
        if (startDate == null) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty Start Date");
        } else if (endDate == null) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty End Date");
        } else if (startDate.isAfter(endDate)) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD,
                    "The event start date should not be after the end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setMaxParticipants(Integer maxParticipants) throws LabdsofCustomException {
        if (maxParticipants == null) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty max participants");
        } else if (maxParticipants < 1) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD,
                    "The event number of participants" + " should be greater or equal than 1");
        }
        this.maxParticipants = maxParticipants;
    }

    public void setEventWebsite(String url) throws LabdsofCustomException {
        Pattern pattern = Pattern.compile(URL_REGEX);
        if (url == null || url.isBlank()) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty Event Website");
        } else if (!pattern.matcher(url.trim()).matches()) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Invalid event website");
        }

        this.eventWebsite = url.trim();
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    private void setEventWorkerList(List<User> ewList) throws LabdsofCustomException {
        if (ewList == null || ewList.isEmpty()) {
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD, "Empty Event Worker list");
        }
        for (User u : ewList) {
            if (!u.hasRole(Role.EVENT_WORKER)) {
                throw new LabdsofCustomException(AppCustomExceptions.EVENT_INVALID_FIELD,
                        "Event workers should have EVENT_WORKER role");
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
