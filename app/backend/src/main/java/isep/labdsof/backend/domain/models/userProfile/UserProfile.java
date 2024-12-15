package isep.labdsof.backend.domain.models.userProfile;

import isep.labdsof.backend.domain.dtos.user.UserProfileDto;
import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.models.BaseEntity;
import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
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

    @Builder.Default
    private int totalPointsAccumulated = 0;
    @Builder.Default
    private int pointsAvailable = 0;

    private void addPoints(final int points) {
        this.totalPointsAccumulated += points;
        this.pointsAvailable += points;
    }

    public boolean removePoints(final int points) {
        if (this.pointsAvailable < points) {
            return false;
        } else {
            this.pointsAvailable -= points;
            return true;
        }
    }

    public void addPointsForReaction() {
        addPoints(1);
    }

    public void addPointsForReportIssue() {
        addPoints(2);
    }

    public void addPointsForIssueResolved() {
        addPoints(3);
    }

    public void addPointsForPraise() {
        addPoints(2);
    }

    public boolean addAttendedEvent(Event event) throws LabdsofCustomException {
        if (attendedEvents.contains(event))
            throw new LabdsofCustomException(AppCustomExceptions.EVENT_ALREADY_ATTENDED, "This event is already in your attended list");
        return attendedEvents.add(event);
    }

    public UserProfileDto toDto() {
        return UserProfileDto.builder()
                .user(user.toDto())
                .attendedEvents(attendedEvents.stream().map(Event::toDto).toList())
                .totalPointsAccumulated(totalPointsAccumulated)
                .pointsAvailable(pointsAvailable)
                .build();
    }


}