package isep.labdsof.backend.domain.dtos;

import isep.labdsof.backend.domain.responses.MessageDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class RegisterUserDto {
    private boolean userCreated;
    private boolean failedToCreateUser;
    @Builder.Default
    private HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private List<MessageDto> messages;
}
