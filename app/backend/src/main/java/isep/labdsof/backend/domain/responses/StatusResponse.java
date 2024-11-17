package isep.labdsof.backend.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class StatusResponse {
    private boolean success;
    private List<MessageDto> messages;
}
