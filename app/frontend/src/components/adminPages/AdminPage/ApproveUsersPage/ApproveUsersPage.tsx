import React, { useEffect, useState } from "react";
import styles from "./ApproveUsersPage.module.css";
import HttpService from "../../../../utils/http";
import {
  listRoleRequestsRequest,
  roleRequestDto,
} from "../../../../utils/types";
import CheckIcon from "@mui/icons-material/Check";
import DeleteIcon from "@mui/icons-material/Delete";

const ApproveUsersPage: React.FC = () => {
  const [requests, setRequests] = useState<roleRequestDto[]>([]);

  useEffect(() => {
    const fetchRequests = async () => {
      const http = new HttpService();
      const response = await http.getPrivate<listRoleRequestsRequest>(
        "/admin/approvals/roles"
      );
      if (response.body) {
        setRequests(response.body.requests);
      }
    };
    fetchRequests();
  }, []);

  const handleApprove = (email: string, role: string) => {
    // Implement approve logic
    console.log(`Approved: ${email} for role ${role}`);
    //TODO: Call the backend to approve the user
  };

  const handleDeny = (email: string, role: string) => {
    // Implement deny logic
    console.log(`Denied: ${email} for role ${role}`);
    //TODO: Call the backend to deny the user
  };

  return (
    <div className={styles.ApproveUsersPage}>
      <h1>Aprovar Utilizadores</h1>
      <table className={styles.ApproveUsersTable}>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Email</th>
            <th>Papel Solicitado</th>
            <th>Justificação</th>
            <th>Ação</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((request) => (
            <tr key={request.name + request.justification}>
              <td>{request.name}</td>
              <td>{request.email}</td>
              <td>{request.role}</td>
              <td>{request.justification}</td>
              <td>
                <button
                  className={styles.approve}
                  onClick={() => handleApprove(request.email, request.role)}
                >
                  <CheckIcon />
                </button>
                <button
                  className={styles.deny}
                  onClick={() => handleDeny(request.email, request.role)}
                >
                  <DeleteIcon />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ApproveUsersPage;
