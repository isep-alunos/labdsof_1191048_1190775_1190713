package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.RegisterUserDto;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.models.user.UserProfile;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    @Override
    public RegisterUserDto register(final String email, final String name, final boolean isEventWorker,
                                    final String eventWorkerInfo, final boolean isEventManager,
                                    final String eventManagerInfo) {

        final Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            final List<MessageDto> messages = new ArrayList<>();
            if (user.get().isWaitingForApprovalForEventWorker()) {
                messages.add(new MessageDto("User is already waiting for approval as an Event Worker", MessageCriticality.WARNING));
            }
            if (user.get().isWaitingForApprovalForEventManager()) {
                messages.add(new MessageDto("User is already waiting for approval as an Event Manager", MessageCriticality.WARNING));
            }
            if (!messages.isEmpty()) {
                return RegisterUserDto.builder()
                        .failedToCreateUser(true)
                        .errorStatus(HttpStatus.BAD_REQUEST)
                        .messages(messages)
                        .build();
            } else {
                return RegisterUserDto.builder()
                        .failedToCreateUser(true)
                        .errorStatus(HttpStatus.BAD_REQUEST)
                        .messages(List.of(new MessageDto("User already exists", MessageCriticality.ERROR)))
                        .build();
            }
        }
        final User userResult = userRepository.save(User.builder()
                .email(email)
                .nome(name)
                .roles(Collections.singletonList(Role.USER))
                .isWaitingForApprovalForEventWorker(isEventWorker)
                .eventWorkerJustification(eventWorkerInfo)
                .isWaitingForApprovalForEventManager(isEventManager)
                .eventManagerJustification(eventManagerInfo)
                .build());

        userProfileRepository.save(UserProfile.builder()
                .user(userResult)
                .build());

        final List<MessageDto> messages = new ArrayList<>(List.of(new MessageDto("User registered successfully.")));
        if (isEventWorker) {
            messages.add(new MessageDto("Waiting for approval to be an Event Worker", MessageCriticality.WARNING));
        }
        if (isEventManager) {
            messages.add(new MessageDto("Waiting for approval to be an Event Manager", MessageCriticality.WARNING));
        }
        return RegisterUserDto.builder()
                .userCreated(true)
                .messages(messages)
                .build();
    }
}
