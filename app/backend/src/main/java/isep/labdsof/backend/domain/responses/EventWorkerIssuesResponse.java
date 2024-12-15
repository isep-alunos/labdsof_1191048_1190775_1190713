package isep.labdsof.backend.domain.responses;

import isep.labdsof.backend.domain.dtos.event.EventDto;
import isep.labdsof.backend.domain.dtos.issue.IssueDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventWorkerIssuesResponse {
    private EventDto event;
    private List<IssueDto> issues;
}
