package isep.labdsof.backend.domain.responses.admin;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ListRoleRequestsRequest(List<RoleRequestDto> requests) {

}
