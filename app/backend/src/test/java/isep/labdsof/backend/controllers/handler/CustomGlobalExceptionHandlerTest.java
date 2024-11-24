
package isep.labdsof.backend.controllers.handler;

import isep.labdsof.backend.domain.exceptions.AppCustomExceptions;
import isep.labdsof.backend.domain.exceptions.LabdsofCustomException;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomGlobalExceptionHandlerTest {

    private CustomGlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        handler = new CustomGlobalExceptionHandler();
    }

    @Test
    public void testCustomHandleNotValid() {
        final MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        final WebRequest request = mock(WebRequest.class);
        final BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(request.getDescription(false)).thenReturn("uri=/test");

        final ResponseEntity<Object> response = handler.customHandleNotValid(ex, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        final Map<String, Object> body = (Map<String, Object>) response.getBody();
        assert body != null;
        assertEquals(HttpStatus.BAD_REQUEST, body.get("status"));
        assertEquals("uri=/test", body.get("instance"));
    }

    @Test
    public void testHandleDataIntegrityViolationException() {
        final ResponseEntity<MessageDto> response = handler.handleDataIntegrityViolationException();
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("A data integrity error occurred", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(MessageCriticality.ERROR, response.getBody().getCriticality());
    }

    @Test
    public void testLabdsoftCustomExceptionHandler() {
        final LabdsofCustomException ex = new LabdsofCustomException(AppCustomExceptions.USER_NOT_FOUND, "User not found");
        final MessageCriticality criticality = MessageCriticality.ERROR;
        final HttpStatus status = HttpStatus.NOT_FOUND;

        final ResponseEntity<MessageDto> response = handler.labdsoftCustomExceptionHandler(ex);
        assertEquals(status, response.getStatusCode());
        assertEquals("User not found", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(criticality, response.getBody().getCriticality());
    }
}