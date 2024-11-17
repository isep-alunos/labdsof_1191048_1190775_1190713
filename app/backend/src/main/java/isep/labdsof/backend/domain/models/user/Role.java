package isep.labdsof.backend.domain.models.user;

public enum Role {
    EVENT_MANAGER("EVENT_MANAGER"),
    EVENT_WORKER("EVENT_WORKER"),
    ADMIN("ADMIN"),
    USER("USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}