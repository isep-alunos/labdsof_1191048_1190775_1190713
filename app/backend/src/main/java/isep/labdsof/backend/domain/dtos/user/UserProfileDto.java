package isep.labdsof.backend.domain.dtos.user;

import isep.labdsof.backend.domain.dtos.event.EventDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfileDto {
    private UserDto user;
    private List<EventDto> attendedEvents;
    private int totalPointsAccumulated;
    private int pointsAvailable;
}