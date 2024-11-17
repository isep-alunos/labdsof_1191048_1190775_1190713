package isep.labdsof.backend.controllers;

import isep.labdsof.backend.domain.dtos.admin.RoleRequestBasicDto;
import isep.labdsof.backend.domain.responses.StatusResponse;
import isep.labdsof.backend.domain.responses.admin.ListRoleRequestsRequest;
import isep.labdsof.backend.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/approvals/roles")
    public ResponseEntity<ListRoleRequestsRequest> listRoleRequests() {
        final ListRoleRequestsRequest response = adminService.listAllUsersWaitingForApprovals();

        if (response.getRequests().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/approvals/roles/approve")
    public ResponseEntity<StatusResponse> approveRoleRequest(@RequestBody final RoleRequestBasicDto body) {
        final StatusResponse response = adminService.approveRoleRequest(body);
        return ResponseEntity.ok(response);
    }

}
