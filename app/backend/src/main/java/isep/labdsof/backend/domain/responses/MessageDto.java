package isep.labdsof.backend.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
    final private String message;
    final private MessageCriticality criticality;

    public MessageDto(final String message) {
        this.message = message;
        criticality = MessageCriticality.INFO;
    }
}
