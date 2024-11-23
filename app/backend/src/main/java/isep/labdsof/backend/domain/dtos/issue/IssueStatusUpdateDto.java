package isep.labdsof.backend.domain.dtos.issue;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IssueStatusUpdateDto {
    private LocalDateTime updateTime;
    private String description;
    private String status;
}
