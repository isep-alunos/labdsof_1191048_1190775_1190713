
package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.RegisterUserDto;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.services.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private final OpaqueTokenIntrospector introspector = Mockito.mock(OpaqueTokenIntrospector.class);
    private final WebClient revocationClient = Mockito.mock(WebClient.class);
    private final AuthService authService = Mockito.mock(AuthService.class);
    private final AuthController authController = new AuthController(introspector, revocationClient, authService);

    @Test
    public void testRegister() {
        final String email = "test@example.com";
        final String name = "Test User";
        final boolean isEventWorker = false;
        final String eventWorkerInfo = "";
        final boolean isEventManager = false;
        final String eventManagerInfo = "";

        final RegisterUserDto registerUserDto = RegisterUserDto.builder()
                .userCreated(true)
                .messages(List.of(new MessageDto("User created", MessageCriticality.INFO)))
                .build();
        when(authService.register(email, name, isEventWorker, eventWorkerInfo, isEventManager, eventManagerInfo)).thenReturn(registerUserDto);

        ResponseEntity<StatusResponse> response = authController.register(email, name, isEventWorker, eventWorkerInfo, isEventManager, eventManagerInfo);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(StatusResponse.builder()
                .success(true)
                .messages(registerUserDto.getMessages())
                .build(), response.getBody());
    }
}