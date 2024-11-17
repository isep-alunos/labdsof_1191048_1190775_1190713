import React, { useEffect, useState } from "react";
import styles from "./ApproveUsersPage.module.css";
import HttpService from "../../../../utils/http";
import {
  listRoleRequestsRequest,
  roleRequestDto,
  registerResponse,
  messageDto,
} from "../../../../utils/types";
import CheckIcon from "@mui/icons-material/Check";
import DeleteIcon from "@mui/icons-material/Delete";
import { useAlert } from "../../../../utils/alerts/AlertContext";

const ApproveUsersPage: React.FC = () => {
  const [requests, setRequests] = useState<roleRequestDto[]>([]);
  const alert = useAlert();

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

  const sendAlert = (message: messageDto) => {
    alert.addAlert(message);
  };

  const handleApprove = async (email: string, role: string) => {
    const http = new HttpService();
    const response = await http.putPrivate<registerResponse>(
      "/admin/approvals/roles/approve",
      { email, role }
    );
    if (response.body) {
      for (let message of response.body.messages ?? []) {
        sendAlert(message);
      }
      if (response.body.success) {
        setRequests((prevRequests) =>
          prevRequests.filter(
            (request) =>
              request.email + ":" + request.role !== email + ":" + role
          )
        );
      }
    }
  };

  const handleDeny = async (email: string, role: string) => {
    const http = new HttpService();
    const response = await http.putPrivate<registerResponse>(
      "/admin/approvals/roles/reject",
      { email, role }
    );
    if (response.body) {
      for (let message of response.body.messages ?? []) {
        sendAlert(message);
      }
      if (response.body.success) {
        setRequests((prevRequests) =>
          prevRequests.filter((request) => request.email !== email)
        );
      }
    }
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
