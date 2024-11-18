package isep.labdsof.backend.services;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestBasicDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.domain.responses.admin.ListRoleRequestsRequest;

public interface AdminService {
    ListRoleRequestsRequest listAllUsersWaitingForApprovals();

    StatusResponse approveRoleRequest(final RoleRequestBasicDto body);

    StatusResponse rejectRoleRequest(final RoleRequestBasicDto body);
}
