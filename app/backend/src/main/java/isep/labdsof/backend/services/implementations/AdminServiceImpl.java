package isep.labdsof.backend.services.implementations;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestBasicDto;
import isep.labdsof.backend.domain.dtos.admin.RoleRequestDto;
import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import isep.labdsof.backend.domain.responses.MessageCriticality;
import isep.labdsof.backend.domain.responses.MessageDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.domain.responses.admin.ListRoleRequestsRequest;
import isep.labdsof.backend.repositories.UserProfileRepository;
import isep.labdsof.backend.repositories.UserRepository;
import isep.labdsof.backend.services.AdminService;
import isep.labdsof.backend.services.EmailService;
import isep.labdsof.backend.services.EventService;
import isep.labdsof.backend.services.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static isep.labdsof.backend.domain.models.user.Role.EVENT_MANAGER;

@Service
@Slf4j
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final EmailService emailService;

    private static final String roleAppealResultEmailTitle = "Role Appeal Result";
    private static final String roleAppealResultEmailBody = """
Dear %s,

Your registration appeal for the role "%s" has been reviewed. The result of your appeal is as follows:

Result: %s

If you have any questions or require further assistance, please do not hesitate to contact our support team.

Best regards,
Porto Eventos
""";

    @Override
    public ListRoleRequestsRequest listAllUsersWaitingForApprovals() {
        final List<User> usersToApprove = userRepository.findAllUsersWaitingForApproval();

        return ListRoleRequestsRequest.builder().requests(usersToApprove.stream().map(RoleRequestDto::fromUser).flatMap(List::stream).toList()).build();
    }

    @Override
    public StatusResponse approveRoleRequest(final RoleRequestBasicDto body) {
        final Optional<User> userOptional = userRepository.findUserByEmail(body.getEmail());
        if (userOptional.isEmpty()) {
            return StatusResponse.builder().success(false).messages(List.of(new MessageDto("User not found", MessageCriticality.ERROR))).build();
        }
        final User user = userOptional.get();
        final Optional<Role> roleApplied = Role.fromString(body.getRole());

        if (roleApplied.isEmpty()) {
            return StatusResponse.builder().success(false).messages(List.of(new MessageDto("Role not found", MessageCriticality.ERROR))).build();
        }

        final List<MessageDto> messages = new ArrayList<>();
        final List<Role> userRoles = new ArrayList<>(user.getRoles());

        switch (roleApplied.get()) {
            case EVENT_WORKER -> {
                if (userRoles.contains(Role.EVENT_WORKER)) {
                    messages.add(new MessageDto("User already has Event Worker role", MessageCriticality.WARNING));
                } else {
                    user.setWaitingForApprovalForEventWorker(false);
                    messages.add(new MessageDto("Event Worker role approved", MessageCriticality.INFO));
                    userRoles.add(Role.EVENT_WORKER);
                    user.setRoles(userRoles);
                    userRepository.save(user);
                    emailService.sendMail(
                            user.getEmail(),
                            roleAppealResultEmailTitle,
                            roleAppealResultEmailBody.formatted(user.getNome(), body.getRole(), "APPROVED"));
                    messages.add(new MessageDto("Event Worker role added", MessageCriticality.INFO));
                }
            }
            case EVENT_MANAGER -> {
                if (userRoles.contains(EVENT_MANAGER)) {
                    messages.add(new MessageDto("User already has Event Manager role", MessageCriticality.WARNING));
                } else {
                    user.setWaitingForApprovalForEventManager(false);
                    messages.add(new MessageDto("Event Manager role approved", MessageCriticality.INFO));
                    userRoles.add(EVENT_MANAGER);
                    user.setRoles(userRoles);
                    userRepository.save(user);
                    emailService.sendMail(
                            user.getEmail(),
                            roleAppealResultEmailTitle,
                            roleAppealResultEmailBody.formatted(user.getNome(), body.getRole(), "APPROVED"));
                    messages.add(new MessageDto("Event Manager role added", MessageCriticality.INFO));
                }
            }
        }

        return StatusResponse.builder().success(true).messages(messages).build();
    }

    @Override
    @Transactional
    public StatusResponse rejectRoleRequest(RoleRequestBasicDto body) {
        final Optional<User> userOptional = userRepository.findUserByEmail(body.getEmail());
        if (userOptional.isEmpty()) {
            return StatusResponse.builder().success(false).messages(List.of(new MessageDto("User not found", MessageCriticality.ERROR))).build();
        }
        final User user = userOptional.get();
        final Optional<Role> roleApplied = Role.fromString(body.getRole());

        if (roleApplied.isEmpty()) {
            return StatusResponse.builder().success(false).messages(List.of(new MessageDto("Role not found", MessageCriticality.ERROR))).build();
        }

        final List<MessageDto> messages = new ArrayList<>();

        switch (roleApplied.get()) {
            case EVENT_WORKER -> {
                if (user.isWaitingForApprovalForEventWorker()) {
                    messages.add(new MessageDto("Event Worker role rejected. User was removed from the database.", MessageCriticality.INFO));
                    userProfileRepository.deleteUserProfileByUser(user);
                    userRepository.delete(user);
                    emailService.sendMail(
                            user.getEmail(),
                            roleAppealResultEmailTitle,
                            roleAppealResultEmailBody.formatted(user.getNome(), body.getRole(), "REJECTED"));
                } else {
                    messages.add(new MessageDto("User didn't requested for \"Event Worker\" role", MessageCriticality.WARNING));
                }
            }
            case EVENT_MANAGER -> {
                if (user.isWaitingForApprovalForEventManager()) {
                    messages.add(new MessageDto("Event Manager role rejected. User was removed from the database.", MessageCriticality.INFO));
                    userProfileRepository.deleteUserProfileByUser(user);
                    userRepository.delete(user);
                    emailService.sendMail(
                            user.getEmail(),
                            roleAppealResultEmailTitle,
                            roleAppealResultEmailBody.formatted(user.getNome(), body.getRole(), "REJECTED"));
                } else {
                    messages.add(new MessageDto("User didn't requested for \"Event Manager\" role", MessageCriticality.WARNING));
                }
            }
        }

        return StatusResponse.builder().success(true).messages(messages).build();
    }
}
