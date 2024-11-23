package isep.labdsof.backend.domain.dtos.issue;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class IssueDto {
    private String id;
    private LocalDateTime creationDate;
    private String title;
    private String description;
    private List<IssueStatusUpdateDto> issueStatusUpdateList;
    private IssueLocationDto location;
    private String eventName;
}
