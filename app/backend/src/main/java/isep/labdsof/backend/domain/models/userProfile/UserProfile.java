package isep.labdsof.backend.domain.models.userProfile;

import isep.labdsof.backend.domain.exceptions.EventAlreadyAttendedException;
import isep.labdsof.backend.domain.exceptions.EventInvalidFieldException;
import isep.labdsof.backend.domain.models.BaseEntity;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "user_profile")
@SuperBuilder
@Entity
@NoArgsConstructor
public class UserProfile extends BaseEntity {

    public UserProfile(UUID id, User user) {
        super(id);
        setUser(user);
        this.attendedEvents = new ArrayList<>();
    }

    @OneToOne
    private User user;

    @OneToMany(orphanRemoval = true)
    private List<Event> attendedEvents;

    // TODO: When developing gamification, add points here

    private void setUser(User user) {
        this.user = user;
    }

    public boolean addAttendedEvent(Event event) throws EventAlreadyAttendedException {
        if(attendedEvents.contains(event))
            throw new EventAlreadyAttendedException("This event is already in your attended list");
        return attendedEvents.add(event);
    }
}