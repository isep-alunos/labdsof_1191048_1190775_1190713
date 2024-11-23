package isep.labdsof.backend.domain.dtos.issue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssueLocationDto {
    private String location;
}
