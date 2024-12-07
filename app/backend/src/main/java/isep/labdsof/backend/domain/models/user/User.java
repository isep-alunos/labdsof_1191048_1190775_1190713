package isep.labdsof.backend.domain.models.user;

import isep.labdsof.backend.convertor.StringCryptoConverter;
import isep.labdsof.backend.domain.dtos.user.UserDto;
import isep.labdsof.backend.domain.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "user_account")
@SuperBuilder
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

    public User(final UUID id, final String email, final String nome, final List<Role> roles) {
        super(id);
        this.email = email;
        this.nome = nome;
        this.roles = new ArrayList<>(roles);
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

    public UserDto toDto() {
        return UserDto.builder()
                .email(this.email)
                .name(this.nome)
                .build();
    }
}