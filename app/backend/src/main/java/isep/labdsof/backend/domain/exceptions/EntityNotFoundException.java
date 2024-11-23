package isep.labdsof.backend.domain.exceptions;

public class EntityNotFoundException extends Exception {

    private String entity;

    public EntityNotFoundException(String entity, String message) {
        super(message);
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}
