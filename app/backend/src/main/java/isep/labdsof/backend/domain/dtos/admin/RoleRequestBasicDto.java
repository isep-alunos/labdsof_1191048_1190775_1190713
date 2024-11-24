package isep.labdsof.backend.domain.dtos.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoleRequestBasicDto {
    private String email;
    private String role;
}
