package isep.labdsof.backend.domain.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class LabdsofCustomException extends Exception {

    private final AppCustomExceptions appCustomExceptions;
    private final String message;

    public LabdsofCustomException(final AppCustomExceptions appCustomExceptions, final String message) {
        super(message);
        this.appCustomExceptions = appCustomExceptions;
        this.message = message;
    }

    public LabdsofCustomException(final AppCustomExceptions appCustomExceptions) {
        super(appCustomExceptions.getDefaultMessage());
        this.appCustomExceptions = appCustomExceptions;
        this.message = appCustomExceptions.getDefaultMessage();
    }
}
