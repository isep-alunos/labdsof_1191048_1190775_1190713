package isep.labdsof.backend.domain.exceptions;

import isep.labdsof.backend.domain.models.event.EventMap;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

@Getter
public enum AppCustomExceptions {

    ENTITY_NOT_FOUND("Entity not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND),
    EVENT_ALREADY_ATTENDED("Entity already attended", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    EVENT_INVALID_FIELD("Invalid field", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    EVENT_NOT_FOUND("Event not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND),
    ISSUE_INVALID_FIELD("Invalid field", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    MARK_PRESENCE_NOT_NEAR_EVENT("User is not near the event", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("User not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND),
    ISSUE_NOT_FOUND("Issue not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND),
    ERROR_COMPRESSING_IMAGE("Error compressing image", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    ERROR_DECOMPRESSING_IMAGE("Error decompressing image", MessageCriticality.ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_IMAGE_TYPE("Invalid image type. Image types allowed: " + EventMap.ALLOWED_IMAGE_TYPES.stream().map(s -> s.replace("image/", ".")).collect(Collectors.joining(", ")), MessageCriticality.ERROR, HttpStatus.BAD_REQUEST);


    private final String defaultMessage;
    private final MessageCriticality messageCriticality;
    private final HttpStatus httpStatus;

    AppCustomExceptions(final String defaultMessage, final MessageCriticality messageCriticality, final HttpStatus httpStatus) {
        this.defaultMessage = defaultMessage;
        this.messageCriticality = messageCriticality;
        this.httpStatus = httpStatus;
    }

}
