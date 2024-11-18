package isep.labdsof.backend.domain.dtos.admin;

import isep.labdsof.backend.domain.models.user.Role;
import isep.labdsof.backend.domain.models.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RoleRequestDto {

    private String name;
    private String email;
    private String role;
    private String justification;


    public static List<RoleRequestDto> fromUser(final User user) {
        final List<RoleRequestDto> users = new ArrayList<>();
        if (user.isWaitingForApprovalForEventWorker()) {
            users.add(new RoleRequestDto(
                    user.getNome(),
                    user.getEmail(),
                    Role.EVENT_WORKER.getShortRoleDescription(),
                    user.getEventWorkerJustification()));
        }
        if (user.isWaitingForApprovalForEventManager()) {
            users.add(new RoleRequestDto(
                    user.getNome(),
                    user.getEmail(),
                    Role.EVENT_MANAGER.getShortRoleDescription(),
                    user.getEventWorkerJustification()));
        }
        return users;
    }
}
