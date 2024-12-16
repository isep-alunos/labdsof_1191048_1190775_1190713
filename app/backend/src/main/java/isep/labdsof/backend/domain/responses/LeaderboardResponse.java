package isep.labdsof.backend.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LeaderboardResponse {
    private List<LeaderboardEntryDto> leaderboardPointsAccumulated;
    private List<LeaderboardEntryDto> leaderboardReportedIssuesResolved;
}
