package isep.labdsof.backend.domain.models.user;

import isep.labdsof.backend.convertor.StringCryptoConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    private boolean isWaitingForApprovalForEventWorker = false;
    private String eventWorkerJustification;
    private boolean isWaitingForApprovalForEventManager = false;
    private String eventManagerJustification;
}