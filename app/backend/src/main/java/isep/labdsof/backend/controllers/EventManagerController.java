package isep.labdsof.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event-manager")
@RequiredArgsConstructor
public class EventManagerController {

    /*
    @PostMapping("/reserva")
    public ResponseEntity<MessageDto> createReserva(@AuthenticationPrincipal(expression = "name") String userEmail,
                                                    @Valid @RequestBody final RequestCreateReservaBody requestCreateReservaBody) {
        try {
            reservaService.createReserva(requestCreateReservaBody, userEmail);
            return ResponseEntity.status(201).body(new MessageDto("Reserva created successfully"));
        } finally {
            // Nullify the sensitive data after use
            if (userEmail != null) {
                Arrays.fill(userEmail.toCharArray(), '\0');
                userEmail = null;
            }
        }
    }
     */




}
