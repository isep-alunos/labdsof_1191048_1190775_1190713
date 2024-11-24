package isep.labdsof.backend.controllers.handler;


import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> customHandleNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("instance", request.getDescription(false));

        //Get all errors
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(HashMap::new, (m, error) -> m.put(error.getField(), error.getDefaultMessage()), HashMap::putAll);

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MessageDto> handleDataIntegrityViolationException() {
        return new ResponseEntity<>(new MessageDto("A data integrity error occurred", MessageCriticality.ERROR), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LabdsofCustomException.class)
    public ResponseEntity<MessageDto> labdsoftCustomExceptionHandler(final LabdsofCustomException ex) {
        return new ResponseEntity<>(new MessageDto(ex.getMessage(),
                ex.getAppCustomExceptions().getMessageCriticality()),
                ex.getAppCustomExceptions().getHttpStatus());
    }

}