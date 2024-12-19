
package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.responses.LeaderboardEntryDto;
import isep.labdsof.backend.domain.responses.LeaderboardResponse;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.UserProfileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PublicControllerTest {

    private final EventService eventService = Mockito.mock(EventService.class);
    private final UserProfileService userProfileService = Mockito.mock(UserProfileService.class);
    private final PublicController publicController = new PublicController(eventService, userProfileService);

    @Test
    public void testGetEvents() {
        final List<EventDto> events = Collections.singletonList(EventDto.builder().build());
        when(eventService.getEvents()).thenReturn(events);

        ResponseEntity<List<EventDto>> response = publicController.getEvents();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(events, response.getBody());
    }

    @Test
    public void testGetLeaderboard() {
        List<LeaderboardEntryDto> pointsAccumulated = Collections.singletonList(
                new LeaderboardEntryDto("User1", 100)
        );
        List<LeaderboardEntryDto> issuesResolved = Collections.singletonList(
                new LeaderboardEntryDto("User2", 50)
        );
        LeaderboardResponse leaderboardResponse = LeaderboardResponse.builder()
                .leaderboardPointsAccumulated(pointsAccumulated)
                .leaderboardReportedIssuesResolved(issuesResolved)
                .build();

        when(userProfileService.getLeaderboard()).thenReturn(leaderboardResponse);

        ResponseEntity<LeaderboardResponse> response = publicController.getLeaderboard();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(leaderboardResponse, response.getBody());
    }
}