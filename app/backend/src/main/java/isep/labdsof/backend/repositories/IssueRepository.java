package isep.labdsof.backend.repositories;

import isep.labdsof.backend.domain.models.event.Event;
import isep.labdsof.backend.domain.models.issue.Issue;
import isep.labdsof.backend.domain.responses.LeaderboardEntryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IssueRepository extends JpaRepository<Issue, UUID> {

    List<Issue> getIssueByEvent(Event event);
    List<Issue> getIssuesByEvent_Name(String event_name);

    @Query("SELECT new isep.labdsof.backend.domain.responses.LeaderboardEntryDto(i.userReporter.nome, COUNT(i)) " +
            "FROM Issue i " +
            "JOIN i.issueStatusUpdateList sl " +
            "WHERE sl.status = 'RESOLVED' " +
            "GROUP BY i.userReporter " +
            "HAVING COUNT(i) > 0 " +
            "ORDER BY COUNT(i) DESC")
    List<LeaderboardEntryDto> getTopReportedResolvedIssues(Pageable pageable);
}