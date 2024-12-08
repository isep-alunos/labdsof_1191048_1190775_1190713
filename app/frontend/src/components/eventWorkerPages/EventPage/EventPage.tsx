import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import styles from "./EventPage.module.css";
import HttpService from "../../../utils/http";
import { issue, issueStatus, updateIssueRequest } from "../../../utils/types";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
} from "@mui/material";
import Cookies from "universal-cookie";

const EventPage: React.FC = () => {
  const { eventName } = useParams<{ eventName: string }>();
  const [issues, setIssues] = useState<issue[]>([]);
  const [selectedIssue, setSelectedIssue] = useState<issue | null>(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogDescription, setDialogDescription] = useState("");
  const [dialogStatus, setDialogStatus] = useState<issueStatus | null>(null);

  useEffect(() => {
    const fetchIssues = async () => {
      const http = new HttpService();
      const response = await http.getPrivate<issue[]>(
        `/private/eventIssues/${eventName}`
      );
      if (response.body) {
        setIssues(response.body);
      }
    };
    fetchIssues();
  }, [eventName]);

  const handleAssign = async (issueId: string) => {
    const requestBody: updateIssueRequest = {
      issueId,
      status: issueStatus.PENDING,
      description: "Assign worker to issue.",
      assigned: true,
    };
    const http = new HttpService();
    const response = await http.putPrivate<issue>(
      `/event-worker/issues/update`,
      requestBody
    );
    if (response.body) {
      setIssues((prevIssues) =>
        prevIssues.map((issue) =>
          issue.id === issueId ? response.body! : issue
        )
      );
    }
  };

  const handleStatusUpdate = async () => {
    if (selectedIssue && dialogStatus) {
      const http = new HttpService();
      const requestBody: updateIssueRequest = {
        issueId: selectedIssue.id,
        status: dialogStatus,
        description: dialogDescription,
        assigned: true,
      };
      const response = await http.putPrivate<issue>(
        `/event-worker/issues/update`,
        requestBody
      );
      if (response.body) {
        setIssues((prevIssues) =>
          prevIssues.map((issue) =>
            issue.id === selectedIssue.id ? response.body! : issue
          )
        );
        setDialogOpen(false);
        setSelectedIssue(null);
        setDialogDescription("");
        setDialogStatus(null);
      }
    }
  };

  const openDialog = (issue: issue, status: issueStatus) => {
    setSelectedIssue(issue);
    setDialogStatus(status);
    setDialogDescription("");
    setDialogOpen(true);
  };

  const closeDialog = () => {
    setDialogOpen(false);
    setSelectedIssue(null);
    setDialogDescription("");
    setDialogStatus(null);
  };

  const onDragStart = (
    event: React.DragEvent<HTMLDivElement>,
    issueId: string
  ) => {
    event.dataTransfer.setData("issueId", issueId);
  };

  const onDrop = async (
    event: React.DragEvent<HTMLDivElement>,
    status: issueStatus
  ) => {
    const issueId = event.dataTransfer.getData("issueId");
    const issueToUpdate = issues.find((issue) => issue.id === issueId);
    if (issueToUpdate) {
      openDialog(issueToUpdate, status);
    }
  };

  const onDragOver = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
  };

  return (
    <div className={styles.EventPage}>
      <h1>Issues for {eventName}</h1>
      <div className={styles.KanbanBoard}>
        {Object.values(issueStatus).map((status) => (
          <div
            key={status}
            className={styles.Column}
            onDrop={(event) => onDrop(event, status)}
            onDragOver={onDragOver}
          >
            <h3>{status}</h3>
            {issues
              .filter(
                (issue) =>
                  issue.issueStatusUpdateList[
                    issue.issueStatusUpdateList.length - 1
                  ].status === status
              )
              .map((issue) => (
                <div
                  key={issue.id}
                  className={styles.IssueCard}
                  draggable={
                    !!issue.eventWorkerAssigned &&
                    issue.eventWorkerAssigned === new Cookies().get("name") && // Replace "currentUser" with the actual current user identifier
                    status !== issueStatus.RESOLVED &&
                    status !== issueStatus.REJECTED
                  }
                  onDragStart={(event) => onDragStart(event, issue.id)}
                >
                  <h4>{issue.title}</h4>
                  <p>Reactions: {issue.reactions}</p>
                  <p>
                    Assigned to: {issue.eventWorkerAssigned || "Unassigned"}
                  </p>
                  {!issue.eventWorkerAssigned && (
                    <button onClick={() => handleAssign(issue.id)}>
                      Assign to Me
                    </button>
                  )}
                  {issue.eventWorkerAssigned && (
                    <>
                      {status === issueStatus.PENDING && (
                        <>
                          <button
                            onClick={() =>
                              openDialog(issue, issueStatus.IN_PROGRESS)
                            }
                          >
                            Start Progress
                          </button>
                          <button
                            onClick={() =>
                              openDialog(issue, issueStatus.REJECTED)
                            }
                          >
                            Reject
                          </button>
                        </>
                      )}
                      {status === issueStatus.IN_PROGRESS && (
                        <button
                          onClick={() =>
                            openDialog(issue, issueStatus.RESOLVED)
                          }
                        >
                          Resolve
                        </button>
                      )}
                    </>
                  )}
                </div>
              ))}
          </div>
        ))}
      </div>

      <Dialog open={dialogOpen} onClose={closeDialog} fullWidth maxWidth="sm">
        <DialogTitle>Update Issue Status</DialogTitle>
        <DialogContent>
          <TextField
            label="Description"
            fullWidth
            multiline
            rows={4}
            value={dialogDescription}
            onChange={(e) => setDialogDescription(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={closeDialog}>Cancel</Button>
          <Button onClick={handleStatusUpdate} color="primary">
            Update
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
};

export default EventPage;
