import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom"; // Import useParams to retrieve the eventName from the URL
import styles from "./EventIssuesPage.module.css"; // Import the styles
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent,
  Box,
} from "@mui/material";
import { criticality, issue } from "../../../../utils/types";
import HttpService from "../../../../utils/http";
import { useAlert } from "../../../../utils/alerts/AlertContext";
import { Info as InfoIcon } from "@mui/icons-material";

const EventIssuesPage: React.FC = () => {
  const { eventName } = useParams<{ eventName: string }>();
  const [issues, setIssues] = useState<issue[]>([]);
  const [filterStatus, setFilterStatus] = useState<string>("ALL");
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
  const [selectedIssue, setSelectedIssue] = useState<issue | null>(null);
  const [errorShown, setErrorShown] = useState(false);
  const [activeStatuses, setActiveStatuses] = useState<string[]>([
    "PENDING",
    "IN_PROGRESS",
  ]);
  const navigate = useNavigate();

  const alert = useAlert();

  useEffect(() => {
    const fetchIssues = async () => {
      try {
        const http = new HttpService();
        const data = await http.getPrivate<issue[]>(
          `/private/eventIssues/${eventName}`
        );
        console.log(data);
        if (data.code >= 400) {
          throw new Error("Error fetching issues.");
        }
        if (data.body) setIssues(data.body);
      } catch (error: any) {
        if (!errorShown) {
          alert.addAlert({
            message:
              "Something went wrong during the fetching the issues of the event.",
            criticality: criticality.ERROR,
          });
          setErrorShown(true);
        }
      }
    };

    fetchIssues();
  }, [eventName]);

  const getStatusLabelStyles = (status: string) => {
    switch (status) {
      case "PENDING":
        return { color: "#FFA500", backgroundColor: "#FFE5B4" }; // Orange
      case "IN_PROGRESS":
        return { color: "#1E90FF", backgroundColor: "#B0E0E6" }; // DodgerBlue
      case "RESOLVED":
        return { color: "#228B22", backgroundColor: "#90EE90" }; // ForestGreen
      case "REJECTED":
        return { color: "#FF4500", backgroundColor: "#FFCCCB" }; // OrangeRed
      default:
        return { color: "#000000", backgroundColor: "#D3D3D3" }; // Black
    }
  };

  const toggleStatus = (status: string) => {
    setActiveStatuses((prevStatuses) =>
      prevStatuses.includes(status)
        ? prevStatuses.filter((s) => s !== status)
        : [...prevStatuses, status]
    );
  };

  const getStatusButtonStyles = (status: string) => {
    const isActive = activeStatuses.includes(status);
    const styles = getStatusLabelStyles(status);
    return {
      color: isActive ? styles.color : "#808080",
      backgroundColor: isActive ? styles.backgroundColor : "#D3D3D3",
      cursor: "pointer",
      padding: "5px 10px",
      borderRadius: "4px",
      margin: "0 5px",
      border: "none",
      outline: "none",
    };
  };

  const filteredIssues = issues.filter((issue) =>
    activeStatuses.includes(
      issue.issueStatusUpdateList[issue.issueStatusUpdateList.length - 1]
        ?.status
    )
  );
  const sortedIssues = filteredIssues.sort((a, b) =>
    sortOrder === "asc"
      ? new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime()
      : new Date(b.creationDate).getTime() - new Date(a.creationDate).getTime()
  );

  const handleFilterChange = (event: SelectChangeEvent<string>) => {
    setFilterStatus(event.target.value as string);
  };

  const handleSortChange = (event: SelectChangeEvent<"asc" | "desc">) => {
    setSortOrder(event.target.value as "asc" | "desc");
  };

  const handleIssueClick = (issue: issue) => {
    setSelectedIssue(issue);
  };

  const handleDialogClose = () => {
    setSelectedIssue(null);
  };

  const handleCreateIssueClick = () => {
    if (eventName) {
      navigate(`/events/${encodeURIComponent(eventName)}/report-issue`);
    }
  };

  const handleReact = async (issueId: string) => {
    try {
      const http = new HttpService();
      const response = await http.putPrivate<issue>(
        `/private/eventIssues/react`,
        { issueId: issueId }
      );
      if (response.body) {
        setIssues((prevIssues) =>
          prevIssues.map((issue) =>
            issue.id === issueId ? { ...issue, ...response.body } : issue
          )
        );
      }
    } catch (error) {
      alert.addAlert({
        message: "Failed to react to the issue.",
        criticality: criticality.ERROR,
      });
    }
  };

  const handlePraise = async (issueId: string) => {
    try {
      const http = new HttpService();
      const response = await http.putPrivate<issue>(
        `/private/eventIssues/praise`,
        { issueId: issueId }
      );
      if (response.body) {
        setIssues((prevIssues) =>
          prevIssues.map((issue) =>
            issue.id === issueId ? { ...issue, ...response.body } : issue
          )
        );
      }
    } catch (error) {
      alert.addAlert({
        message: "Failed to praise the event worker.",
        criticality: criticality.ERROR,
      });
    }
  };

  return (
    <div className={styles.EventIssuesPage}>
      <h1>{eventName ? `${eventName}'s Issues` : "Event Issues"}</h1>
      <div className={styles.Filters}>
        <Box
          className="Filters"
          display="flex"
          gap="20px"
          justifyContent="flex-start"
          alignItems="center"
          mb={2}
          sx={{ flexWrap: "wrap" }}
        >
          <button
            style={getStatusButtonStyles("PENDING")}
            onClick={() => toggleStatus("PENDING")}
          >
            Pending
          </button>
          <button
            style={getStatusButtonStyles("IN_PROGRESS")}
            onClick={() => toggleStatus("IN_PROGRESS")}
          >
            In Progress
          </button>
          <button
            style={getStatusButtonStyles("RESOLVED")}
            onClick={() => toggleStatus("RESOLVED")}
          >
            Resolved
          </button>
          <button
            style={getStatusButtonStyles("REJECTED")}
            onClick={() => toggleStatus("REJECTED")}
          >
            Rejected
          </button>
          <FormControl
            className="FilterControl"
            sx={{ minWidth: 180, maxWidth: "100%" }}
            variant="outlined"
          >
            <InputLabel>Status</InputLabel>
            <Select
              value={filterStatus}
              onChange={handleFilterChange}
              label="Status"
              sx={{
                backgroundColor: "#f5f5f5",
                borderRadius: "4px",
                padding: "8px 12px",
                height: "40px",
              }}
            >
              <MenuItem value="ALL">All</MenuItem>
              <MenuItem value="PENDING">Pending</MenuItem>
              <MenuItem value="IN_PROGRESS">In Progress</MenuItem>
              <MenuItem value="RESOLVED">Resolved</MenuItem>
              <MenuItem value="REJECTED">Rejected</MenuItem>
            </Select>
          </FormControl>

          <FormControl
            className="FilterControl"
            sx={{ minWidth: 180, maxWidth: "100%" }}
            variant="outlined"
          >
            <InputLabel>Sort By</InputLabel>
            <Select
              value={sortOrder}
              onChange={handleSortChange}
              label="Sort By"
              sx={{
                backgroundColor: "#f5f5f5",
                borderRadius: "4px",
                padding: "8px 12px",
                height: "40px",
              }}
            >
              <MenuItem value="asc">Creation Date (Ascending)</MenuItem>
              <MenuItem value="desc">Creation Date (Descending)</MenuItem>
            </Select>
          </FormControl>
          <FormControl
            className="FilterControl"
            sx={{ minWidth: 180, maxWidth: "100%" }}
            variant="outlined"
          >
            <Button
              onClick={handleCreateIssueClick}
              color="secondary"
              variant="outlined"
            >
              Report Issue
            </Button>
          </FormControl>
        </Box>
      </div>

      <table className={styles.IssuesTable}>
        <thead>
          <tr>
            <th></th>
            <th>Title</th>
            <th>Status</th>
            <th>Creation Date</th>
            <th>Reactions</th>
            <th>Event Worker</th>
          </tr>
        </thead>
        <tbody>
          {sortedIssues.map((issue) => (
            <tr key={issue.id} className={styles.TableRow}>
              <td>
                <InfoIcon
                  className={styles.InfoIcon}
                  onClick={() => handleIssueClick(issue)}
                />
              </td>
              <td>{issue.title}</td>
              <td>
                <strong
                  style={{
                    color: getStatusLabelStyles(
                      issue.issueStatusUpdateList[
                        issue.issueStatusUpdateList.length - 1
                      ]?.status
                    ).color,
                    backgroundColor: getStatusLabelStyles(
                      issue.issueStatusUpdateList[
                        issue.issueStatusUpdateList.length - 1
                      ]?.status
                    ).backgroundColor,
                    padding: "2px 8px",
                    borderRadius: "4px",
                  }}
                >
                  {
                    issue.issueStatusUpdateList[
                      issue.issueStatusUpdateList.length - 1
                    ]?.status
                  }
                </strong>
              </td>
              <td>{new Date(issue.creationDate).toLocaleString()}</td>
              <td>
                {issue.reactions}
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    handleReact(issue.id);
                  }}
                  disabled={issue.userReacted || issue.userIsOwner}
                >
                  {issue.userReacted
                    ? "Reacted"
                    : issue.userIsOwner
                    ? "Can't react"
                    : "React"}
                </button>
              </td>
              <td>
                {issue.eventWorkerAssigned ? (
                  issue.issueStatusUpdateList[
                    issue.issueStatusUpdateList.length - 1
                  ]?.status === "RESOLVED" ? (
                    <>
                      {issue.eventWorkerAssigned}
                      <br />
                      Praises: {issue.eventWorkerPraises ?? 0}
                      <br />
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          handlePraise(issue.id);
                        }}
                        disabled={issue.userPraised || issue.userIsWorker}
                      >
                        {issue.userPraised
                          ? "Praised"
                          : issue.userIsWorker
                          ? "Can't praise"
                          : "Praise"}
                      </button>
                    </>
                  ) : (
                    issue.eventWorkerAssigned
                  )
                ) : (
                  "Not assigned"
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedIssue && (
        <Dialog open={true} onClose={handleDialogClose} fullWidth maxWidth="sm">
          <DialogTitle className={styles.DialogTitle}>
            Issue Details
          </DialogTitle>
          <DialogContent className={styles.DialogContent}>
            <p>
              <strong>Title:</strong> {selectedIssue.title}
            </p>
            <p>
              <strong>Description:</strong> {selectedIssue.description}
            </p>
            <p>
              <strong>Location:</strong> {selectedIssue.location.location}
            </p>
            <p>
              <strong>Reactions:</strong> {selectedIssue.reactions}{" "}
            </p>
            <p>
              <strong>Assigned Worker:</strong>{" "}
              {selectedIssue.eventWorkerAssigned || "Unassigned"}
            </p>
            <h4>Status History:</h4>
            <ul>
              {selectedIssue.issueStatusUpdateList.map((update) => (
                <li key={update.updateTime}>
                  <strong
                    style={{
                      color: getStatusLabelStyles(update.status).color,
                      backgroundColor: getStatusLabelStyles(update.status)
                        .backgroundColor,
                      padding: "2px 8px",
                      borderRadius: "4px",
                    }}
                  >
                    {update.status}
                  </strong>{" "}
                  - {update.description} (
                  {new Date(update.updateTime).toLocaleString()})
                </li>
              ))}
            </ul>
          </DialogContent>
          <DialogActions className={styles.DialogActions}>
            <Button onClick={handleDialogClose}>Close</Button>
          </DialogActions>
        </Dialog>
      )}
    </div>
  );
};

export default EventIssuesPage;
