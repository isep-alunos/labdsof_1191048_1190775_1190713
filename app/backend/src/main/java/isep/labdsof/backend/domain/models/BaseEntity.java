package isep.labdsof.backend.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@AllArgsConstructor
@SuperBuilder
@jakarta.persistence.MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public BaseEntity() {
    }
}
