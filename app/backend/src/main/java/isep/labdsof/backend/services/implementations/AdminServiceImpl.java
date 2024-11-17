package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestBasicDto;
import isep.labdsof.backend.domain.dtos.admin.RoleRequestDto;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.domain.responses.admin.ListRoleRequestsRequest;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;


    @Override
    public ListRoleRequestsRequest listAllUsersWaitingForApprovals() {
        final List<User> usersToApprove = userRepository.findAllUsersWaitingForApproval();

        return ListRoleRequestsRequest.builder()
                .requests(usersToApprove.stream()
                        .map(RoleRequestDto::fromUser)
                        .flatMap(List::stream)
                        .toList())
                .build();
    }

    @Override
    public StatusResponse approveRoleRequest(final RoleRequestBasicDto body) {
        final Optional<User> userOptional = userRepository.findUserByEmail(body.getEmail());
        if (userOptional.isEmpty()) {
            return StatusResponse.builder()
                    .success(false)
                    .messages(List.of(new MessageDto("User not found", MessageCriticality.ERROR)))
                    .build();
        }
        final User user = userOptional.get();
        final Optional<Role> roleApplied = Role.fromShortRoleDescription(body.getRole());

        if (roleApplied.isEmpty()) {
            return StatusResponse.builder()
                    .success(false)
                    .messages(List.of(new MessageDto("Role not found", MessageCriticality.ERROR)))
                    .build();
        }

        final List<MessageDto> messages = new ArrayList<>();
        final List<Role> userRoles = user.getRoles();

        switch (roleApplied.get()) {
            case EVENT_WORKER -> {
                user.setWaitingForApprovalForEventWorker(false);
                messages.add(new MessageDto("Event Worker role approved", MessageCriticality.INFO));
                if (userRoles.contains(Role.EVENT_MANAGER)) {
                    messages.add(new MessageDto("User already has Event Manager role", MessageCriticality.WARNING));
                } else {
                    userRoles.add(Role.EVENT_WORKER);
                    messages.add(new MessageDto("Event Worker role added", MessageCriticality.INFO));
                    user.setRoles(userRoles);
                    userRepository.save(user);
                }
            }
            case EVENT_MANAGER -> {
                user.setWaitingForApprovalForEventManager(false);
                messages.add(new MessageDto("Event Manager role approved", MessageCriticality.INFO));
                if (userRoles.contains(Role.EVENT_WORKER)) {
                    messages.add(new MessageDto("User already has Event Worker role", MessageCriticality.WARNING));
                } else {
                    userRoles.add(Role.EVENT_MANAGER);
                    messages.add(new MessageDto("Event Manager role added", MessageCriticality.INFO));
                    user.setRoles(userRoles);
                    userRepository.save(user);
                }
            }
        }

        return StatusResponse.builder()
                .success(true)
                .messages(messages)
                .build();
    }
}
