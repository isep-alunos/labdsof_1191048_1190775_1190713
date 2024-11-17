package isep.labdsof.backend.domain.responses.admin;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListRoleRequestsRequest {

    final private List<RoleRequestDto> requests;
}
