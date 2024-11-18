package isep.labdsof.backend.domain.dtos.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleRequestBasicDto {
    private String email;
    private String role;
}
