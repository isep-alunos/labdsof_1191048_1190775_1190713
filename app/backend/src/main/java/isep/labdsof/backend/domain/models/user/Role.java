package isep.labdsof.backend.domain.models.user;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    EVENT_MANAGER("EVENT_MANAGER", "Event Manager"),
    EVENT_WORKER("EVENT_WORKER", "Event Worker"),
    ADMIN("ADMIN", "Admin"),
    USER("USER", "User");

    private final String role;
    private final String shortRoleDescription;

    Role(final String role, final String shortRoleDescription) {
        this.role = role;
        this.shortRoleDescription = shortRoleDescription;
    }

    public static Optional<Role> fromShortRoleDescription(final String role) {
        return Arrays.stream(Role.values())
                .filter(r -> r.getShortRoleDescription().equals(role))
                .findFirst();
    }

    public String getRole() {
        return role;
    }

    public String getShortRoleDescription() {
        return shortRoleDescription;
    }
}