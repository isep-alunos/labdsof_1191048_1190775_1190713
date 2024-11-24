
package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestBasicDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.domain.responses.admin.ListRoleRequestsRequest;
import isep.labdsof.backend.services.AdminService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AdminControllerTest {

    private final AdminService adminService = Mockito.mock(AdminService.class);
    private final AdminController adminController = new AdminController(adminService);

    @Test
    public void testListRoleRequests() {
        final ListRoleRequestsRequest listRoleRequestsRequest = new ListRoleRequestsRequest(Collections.emptyList());
        when(adminService.listAllUsersWaitingForApprovals()).thenReturn(listRoleRequestsRequest);

        ResponseEntity<ListRoleRequestsRequest> response = adminController.listRoleRequests();

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    public void testApproveRoleRequest() {
        final RoleRequestBasicDto roleRequestBasicDto = new RoleRequestBasicDto("email@email", "EVENT_WORKER");
        final StatusResponse statusResponse = new StatusResponse(true, Collections.emptyList());
        when(adminService.approveRoleRequest(roleRequestBasicDto)).thenReturn(statusResponse);

        ResponseEntity<StatusResponse> response = adminController.approveRoleRequest(roleRequestBasicDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(statusResponse, response.getBody());
    }

    @Test
    public void testRejectRoleRequest() {
        final RoleRequestBasicDto roleRequestBasicDto = new RoleRequestBasicDto("email@email", "EVENT_WORKER");
        final StatusResponse statusResponse = new StatusResponse(true, Collections.emptyList());
        when(adminService.rejectRoleRequest(roleRequestBasicDto)).thenReturn(statusResponse);

        ResponseEntity<StatusResponse> response = adminController.rejectRoleRequest(roleRequestBasicDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(statusResponse, response.getBody());
    }
}