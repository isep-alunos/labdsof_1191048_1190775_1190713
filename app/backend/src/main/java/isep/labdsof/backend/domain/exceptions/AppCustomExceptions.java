package isep.labdsof.backend.domain.exceptions;

import isep.labdsof.backend.domain.responses.MessageCriticality;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AppCustomExceptions {

    ENTITY_NOT_FOUND("Entity not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND),
    EVENT_ALREADY_ATTENDED("Entity already attended", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    EVENT_INVALID_FIELD("Invalid field", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    EVENT_NOT_FOUND("Event not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND),
    ISSUE_INVALID_FIELD("Invalid field", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    MARK_PRESENCE_NOT_NEAR_EVENT("User is not near the event", MessageCriticality.ERROR, HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("User not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND),
    ISSUE_NOT_FOUND("Issue not found", MessageCriticality.ERROR, HttpStatus.NOT_FOUND);


    private final String defaultMessage;
    private final MessageCriticality messageCriticality;
    private final HttpStatus httpStatus;

    AppCustomExceptions(final String defaultMessage, final MessageCriticality messageCriticality, final HttpStatus httpStatus) {
        this.defaultMessage = defaultMessage;
        this.messageCriticality = messageCriticality;
        this.httpStatus = httpStatus;
    }

}
