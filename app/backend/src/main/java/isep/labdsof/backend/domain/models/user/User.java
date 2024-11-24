package isep.labdsof.backend.domain.models.user;

import isep.labdsof.backend.convertor.StringCryptoConverter;
import isep.labdsof.backend.domain.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "user_account")
@SuperBuilder
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

    public User(UUID id, String email, String nome, List<Role> roles) {
        super(id);
        this.email = email;
        this.nome = nome;
        this.roles = roles;
    }

    @Convert(converter = StringCryptoConverter.class)
    @Column(unique = true)
    private String email;

    @Convert(converter = StringCryptoConverter.class)
    private String nome;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "tblRoles", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @Builder.Default
    private boolean isWaitingForApprovalForEventWorker = false;
    private String eventWorkerJustification;
    @Builder.Default
    private boolean isWaitingForApprovalForEventManager = false;
    private String eventManagerJustification;

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }
}